package com.monke.yandextodo.data.repository

import androidx.lifecycle.MutableLiveData
import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.localStorage.roomModels.RevisionRoom
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.networkService.pojo.TodoItemElement
import com.monke.yandextodo.data.networkService.pojo.TodoItemsList
import com.monke.yandextodo.domain.Constants
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

    suspend fun fetchData() : RepositoryResponse = withContext(Dispatchers.IO){
        // Пробуем получить данные с сервера
        val response = fetchDataFromServer()
        // Если данные были получены, сравниваем ревизии
        if (response.statusCode == 200) {
            val rev = todoItemsDatabase.revisionDao().getLastRevision().first()
            // Если на устройстве нет ревизии, загружаем данные с сервера в локальное хранилище
            if (rev == null) {
                mergeFromServer()
            }
            // Если данные с бд и сервера не равны, отправляем пользователю запрос с выбором источника данных
            else if (!compareDatabaseAndServer(rev))
                return@withContext RepositoryResponse(
                    statusCode = Constants.CODE_NEED_SYNC,
                    message = "Need sync")
            return@withContext RepositoryResponse(
                statusCode = Constants.CODE_REPOSITORY_SUCCESS,
                message = "Success")
        } else {
            // Если не получилось, загружаем данные с устройства и ждем появления интернета
            fetchDataFromDatabase()

            return@withContext RepositoryResponse(
                statusCode = Constants.CODE_NO_NETWORK,
                message = "No network connection"
            )
        }

    }

    private suspend fun compareDatabaseAndServer(localRevision: Int) =
        withContext(Dispatchers.IO) {
            // Проверяем номера ревизий
            if (cacheStorage.getLastKnownRevision() !=
                localRevision)
                return@withContext false

            val localData = todoItemsDatabase.todoItemDao()
                .getTodoItemsList().first().map { TodoItemConverters.roomToModel(it) }
            val serverData = cacheStorage.getTodoItemsList().value!!
            val serverItemsByIdMap = serverData.associateBy { it.id }

            // Проверяем размер списков
            if (localData.size != serverData.size)
                return@withContext false

            // Проверяем наличие заданий с бд в списке сервера
            // Если какого-то элемента нет, значит возвращаем false
            // Если элемент есть, сравниваем его с элементом с сервера
            for (todoItem in localData) {
                if (serverItemsByIdMap[todoItem.id] == null)
                    return@withContext false
                else if (todoItem != serverItemsByIdMap[todoItem.id])
                    return@withContext false
            }
            return@withContext true

        }


    private suspend fun fetchDataFromDatabase() {
        withContext(Dispatchers.IO) {
           val todoItemsList =  todoItemsDatabase.todoItemDao().getTodoItemsList().first()
            cacheStorage.setTodoItemsList(
                todoItemsList.map { TodoItemConverters.roomToModel(it) } as ArrayList<TodoItem>)
        }
    }

    // Мердж данных с сервера в локальную БД
    suspend fun mergeFromServer() {
        // Получаем данные с локального хранилища
        withContext(Dispatchers.IO) {
            // Сравниваем данные с сервера и бд
            val localData = todoItemsDatabase.todoItemDao().getTodoItemsList().first()
            val serverData = cacheStorage.getTodoItemsList().value!!
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
    suspend fun mergeFromDatabase() = withContext(Dispatchers.IO) {
        // Получение данных из локальной БД
        val localData = todoItemsDatabase.todoItemDao().getTodoItemsList().first()
            .map { TodoItemConverters.modelToPojo(TodoItemConverters.roomToModel(it)) }

        lateinit var repositoryResponse: RepositoryResponse

        val list = TodoItemsList(list = localData)

        // Запрос к серверу на изменение списка задач
        val serviceResult = todoItemService.getService().patchTodoItemsList(
            cacheStorage.getLastKnownRevision(),
            list)

        // В случае успешного запроса
        serviceResult.onSuccess {
            val todoItemsList = it

            // Сохраняем новый список задач в кэш
            cacheStorage.clearTodoItemsList()
            saveTodoItemsListToCache(todoItemsList.list.map {
                    todoItem -> TodoItemConverters.modelFromPojo(todoItem)
            })
            // Сохраняем номер последней ревизии в БД и кэш
            updateRevision(todoItemsList.revision)

            repositoryResponse = RepositoryResponse(
                statusCode = 200,
                message = "Success")
        }.onFailure {
            // Получаем ошибку в случае неудачи
            repositoryResponse = RepositoryResponse(
                statusCode = 500,
                message = it.localizedMessage
            )
        }

        repositoryResponse
    }

    // Загрузка данных с сервера в кэш
    private suspend fun fetchDataFromServer() = withContext(Dispatchers.IO) {
        lateinit var response: RepositoryResponse
        // Запрос к серверу на получение списка задач
        val serviceResponse = todoItemService.getService().getTodoItemsList()

        // В случае успеха
        serviceResponse.onSuccess {
            val todoItemsList = it
            // Сохраняем в кэш мапнутый список
            saveTodoItemsListToCache(todoItemsList.list.map {
                todoItem -> TodoItemConverters.modelFromPojo(todoItem)
            })
            // Сохраняем в кэш номер последней ревизии
            setLastKnownRevision(it.revision)

            response = RepositoryResponse(
                statusCode = 200,
                message = "Success"
            )
        }.onFailure {
            // Получаем ошибку в случае неудачи
            response = RepositoryResponse(
                statusCode = 500,
                message = it.localizedMessage
            )
        }

        response
        }

    // Добавление задачи
    suspend fun addTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        // Добавление задачи в кэш
        cacheStorage.addTodoItem(todoItem)
        // Добавление задачи в БД
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))

        lateinit var response: RepositoryResponse

        // Запрос к серверу на добавление задачи
        val serviceResponse = todoItemService.getService().addTodoItem(
                cacheStorage.getLastKnownRevision(),
                TodoItemElement(element = TodoItemConverters.modelToPojo(todoItem)))
        // В случае успеха
        serviceResponse.onSuccess {
            // Обновляем номер ревизии
            updateRevision(it.revision)

            response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success")
        }.onFailure {
            // Получаем ошибку в случае неудачи
            response = RepositoryResponse(
                statusCode = 500,
                message = it.localizedMessage)
        }
        response
    }

    suspend fun deleteTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        // Удаление задачи в кэше
        cacheStorage.deleteTodoItem(todoItem)
        // Удаление задачи в БД
        todoItemsDatabase.todoItemDao().delete(TodoItemConverters.modelToRoom(todoItem))

        lateinit var response: RepositoryResponse
        // Запрос к серверу на удаление задачи
        val serviceResponse = todoItemService.getService().deleteTodoItem(
                todoItem.id,
                cacheStorage.getLastKnownRevision())

        // В случае успеха
        serviceResponse.onSuccess {
            // Обновляем ревизию
            updateRevision(it.revision)
            response = RepositoryResponse(
                statusCode = 200,
                message = "Success")
        }.onFailure {
            // Получаем ошибку в случае неудачи
            response = RepositoryResponse(
                statusCode = 500,
                message = it.localizedMessage)
        }

        response
    }

    fun getTodoItemsList(): MutableLiveData<ArrayList<TodoItem>> {
        return cacheStorage.getTodoItemsList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return cacheStorage.getTodoItemById(id)
    }

    suspend fun setTodoItem(newItem: TodoItem) = withContext(Dispatchers.IO) {
        lateinit var response: RepositoryResponse
        // Обновление задачи в кэше
        cacheStorage.setTodoItem(newItem)
        // Обновление задачи в БД
        todoItemsDatabase.todoItemDao().updateTodoItems(TodoItemConverters.modelToRoom(newItem))

        // Запрос к серверу на обновление задачи
        val serviceResponse = todoItemService.getService().setTodoItem(
                newItem.id,
                cacheStorage.getLastKnownRevision(),
                TodoItemElement(element = TodoItemConverters.modelToPojo(newItem)))

        serviceResponse.onSuccess {
            // Обновление ревизии в кэше и бд
            updateRevision(it.revision)
            response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success")
        }.onFailure {
            // Получаем ошибку в случае неудачи
            response = RepositoryResponse(
                statusCode = 500,
                message = it.localizedMessage)
        }
        response
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

    private suspend fun saveTodoItemsListToCache(todoItemsList: List<TodoItem>?) {
        if (todoItemsList != null) {
            for (todoItem in todoItemsList) {
                cacheStorage.addTodoItem(todoItem)
            }
        }
    }

    private fun updateRevision(newRevision: Int) {
        setLastKnownRevision(newRevision)
        saveLastKnownRevisionToDatabase(newRevision)
    }

    suspend fun synchronizeWithServer() : RepositoryResponse {
        var response = fetchDataFromServer()
        if (response.statusCode != Constants.CODE_REPOSITORY_SUCCESS)
            return response

        var lastKnownRevision = withContext(Dispatchers.IO) {
            todoItemsDatabase.revisionDao().getLastRevision().first()
        }

        if (lastKnownRevision == null || !compareDatabaseAndServer(lastKnownRevision)) {
            mergeFromServer()
        }

        return RepositoryResponse(
            statusCode = 200,
            message = "Success"
        )
    }



}