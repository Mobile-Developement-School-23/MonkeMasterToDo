package com.monke.yandextodo.data.repository

import android.util.Log
import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.localStorage.roomModels.RevisionRoom
import com.monke.yandextodo.data.networkService.pojo.ServiceResponse
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.data.networkService.pojo.TodoItemsList
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.ArrayList

class TodoItemsRepository @Inject constructor(
    private val cacheStorage: TodoItemCacheStorage,
    private val todoItemsDatabase: TodoItemsDatabase,
    private val todoItemService: TodoItemService
) {

    suspend fun fetchData() {
        // Пробуем получить данные с сервера
        val response = fetchDataFromServer()
        // Если данные были получены, сравниваем ревизии
        if (response.statusCode == 200) {
            // Если на устройстве нет ревизии, загружаем данные с сервера в локальное хранилище
            if (todoItemsDatabase.revisionDao().getLastRevision() == null)
                mergeFromServer()
            // Если ревизии не равны, отправляем пользователю запрос с выбором источника данных
            if (cacheStorage.getLastKnownRevision() != todoItemsDatabase.revisionDao().getLastRevision())
                mergeFromServer()
        } else
            // Если не получилось, загружаем данные с устройства и ждем появления интернета
            fetchDataFromDatabase()


    }

    private suspend fun fetchDataFromDatabase() {
        val todoItemsRoom = todoItemsDatabase.todoItemDao().getTodoItemsList().collect {
            cacheStorage.setTodoItemsList(
                it.map { TodoItemConverters.roomToModel(it) } as ArrayList<TodoItem>)
        }

    }

    // Мердж данных с сервера в локальную БД
    private suspend fun mergeFromServer() {
        // Получаем данные с локального хранилища
        withContext(Dispatchers.IO) {
            // Сравниваем данные с сервера и бд
            val localData = todoItemsDatabase.todoItemDao().getTodoItemsList().first()
            val serverData = cacheStorage.getTodoItemsList().first()
            val todoItemsByIdMap = serverData.associateBy { it.id }
            // Если в бд есть задачи, отсутствующие на сервере, удаляем их из бд
            for (todoItem in localData) {
                if (todoItemsByIdMap[todoItem.id] == null) {
                    todoItemsDatabase.todoItemDao().delete(todoItem)
                }
            }
            // Добавляем отсутствующие в бд задачи
            // Если какие-либо задачи были изменены в бд, они будут заменены свежими данными
            for (todoItem in serverData) {
                todoItemsDatabase.todoItemDao()
                    .addTodoItems(TodoItemConverters.modelToRoom(todoItem))
            }

            saveLastKnownRevisionToDatabase(cacheStorage.getLastKnownRevision())

            
        }
    }

    // Мердж данных на сервер с локальной БД
    private suspend fun mergeFromDatabase() = withContext(Dispatchers.IO) {
        val localData = todoItemsDatabase.todoItemDao().getTodoItemsList().first()
            .map { TodoItemConverters.modelToPojo(TodoItemConverters.roomToModel(it)) }

        lateinit var repositoryResponse: RepositoryResponse
        try {
            val serviceResponse = todoItemService.getService().patchTodoItemsList(
                cacheStorage.getLastKnownRevision(),
                TodoItemsList(list = localData))

            if (serviceResponse.isSuccessful && serviceResponse.body() != null) {
                saveToCache(serviceResponse.body()?.list?.map {
                    TodoItemConverters.modelFromPojo(it) })
                saveLastKnownRevisionToDatabase(serviceResponse.body()!!.revision)
                setLastKnownRevision(serviceResponse.body()!!.revision)

                repositoryResponse = RepositoryResponse(
                    statusCode = 200,
                    message = "Success")
            } else
                repositoryResponse = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message())
        } catch (e : Exception) {
            repositoryResponse = RepositoryResponse(
                statusCode = 500,
                message = "Unknown error")
        }

        repositoryResponse

    }

    private suspend fun fetchDataFromServer() =
        withContext(Dispatchers.IO) {
            lateinit var response: RepositoryResponse
            try {
                val serviceResponse = todoItemService.getService().getTodoItemsList()
                if (serviceResponse.isSuccessful) {
                    var todoItemsList =
                        serviceResponse.body()?.list?.map { TodoItemConverters.modelFromPojo(it) }

                    saveToCache(todoItemsList)

                    val revision = serviceResponse.body()?.revision
                    if (revision != null) {
                        setLastKnownRevision(revision)
                    }
                    response = RepositoryResponse(
                        statusCode = 200,
                        message = "Success"
                    )
                } else {
                    response = RepositoryResponse(
                        statusCode = serviceResponse.code(),
                        message = serviceResponse.message()
                    )
                    Log.d(serviceResponse.message(), serviceResponse.code().toString())
                }
            } catch (exception: Exception) {
                Log.d("Error", exception.message + " unit")
                response = RepositoryResponse(
                    statusCode = 500,
                    message = "Unknown error")
            }
            response
        }

    // Добавление задачи
    suspend fun addTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        cacheStorage.addTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))
        lateinit var response: RepositoryResponse

        try {
            val serviceResponse = todoItemService.getService().addTodoItem(
                cacheStorage.getLastKnownRevision(),
                TodoItemContainer(element = TodoItemConverters.modelToPojo(todoItem))
            )
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null) {
                    setLastKnownRevision(lastKnownRevision)
                    saveLastKnownRevisionToDatabase(lastKnownRevision)
                }
                response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success"
                )
            } else {
                // Обработка ошибок
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }

            response
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }


    }


    suspend fun deleteTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        cacheStorage.deleteTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().delete(TodoItemConverters.modelToRoom(todoItem))

        // Удаление задачи на сервере
        lateinit var response: RepositoryResponse
        try {
            val serviceResponse = todoItemService.getService().deleteTodoItem(
                todoItem.id,
                cacheStorage.getLastKnownRevision())
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null) {
                    setLastKnownRevision(lastKnownRevision)
                    saveLastKnownRevisionToDatabase(lastKnownRevision)
                }
                response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success"
                )
            } else {
                // Обработка ошибок
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }

            response
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }

    }

    fun getTodoItemsList(): MutableStateFlow<ArrayList<TodoItem>> {
        return cacheStorage.getTodoItemsList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return cacheStorage.getTodoItemById(id)
    }

    suspend fun setTodoItem(newItem: TodoItem) = withContext(Dispatchers.IO) {
        cacheStorage.setTodoItem(newItem)
        todoItemsDatabase.todoItemDao().updateTodoItems(TodoItemConverters.modelToRoom(newItem))

        lateinit var response: RepositoryResponse
        lateinit var serviceResponse: retrofit2.Response<ServiceResponse>
        try {
            // Запрос на изменение задачи на сервере
            val lastRevision = cacheStorage.getLastKnownRevision()
            serviceResponse = todoItemService.getService().setTodoItem(
                newItem.id,
                lastRevision,
                TodoItemContainer(element = TodoItemConverters.modelToPojo(newItem)))
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null) {
                    setLastKnownRevision(lastKnownRevision)
                    saveLastKnownRevisionToDatabase(lastKnownRevision)
                }
                response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success"
                )
            } else {
                // Обработка ошибок
                Log.d("Error", "${serviceResponse.code()} ${serviceResponse.body()}")
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }

            response
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }

    }

    private fun setLastKnownRevision(revision: Int) {
        cacheStorage.setRevision(revision)
    }

    private fun saveLastKnownRevisionToDatabase(revision: Int) {
        todoItemsDatabase.revisionDao().addRevision(
            RevisionRoom(
                UUID.randomUUID().toString(),
                revision))
    }

    fun saveToCache(todoItemsList: List<TodoItem>?) {
        if (todoItemsList != null) {
            for (todoItem in todoItemsList) {
                cacheStorage.addTodoItem(todoItem)
            }
        }
    }


}