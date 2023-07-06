package com.monke.yandextodo.data.repository

import android.util.Log
import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.networkService.pojo.ServiceResponse
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

class TodoItemsRepository @Inject constructor(
    private val cacheStorage: TodoItemCacheStorage,
    private val todoItemsDatabase: TodoItemsDatabase,
    private val todoItemService: TodoItemService
) {

    suspend fun fetchData() {
        fetchDataFromServer()
    }

    private suspend fun fetchDataDatabase() {
//        val todoItemsRoom = todoItemsDatabase.todoItemDao().getTodoItemsList()
//        val todoItems = ArrayList<TodoItem>()
//
//        for (todoItemRoom in todoItemsRoom) {
//            todoItems.add(TodoItemConverters.roomToModel(todoItemRoom))
//        }
//
//        cacheStorage.addAll(todoItems)
    }

    private suspend fun fetchDataFromServer() {
        withContext(Dispatchers.IO) {
            val serviceResponse = todoItemService.getService().getTodoItemsList()
            lateinit var response: RepositoryResponse
            try {
                if (serviceResponse.isSuccessful) {
                    var todoItemsList =
                        serviceResponse.body()?.list?.map { TodoItemConverters.todoItemFromPojo(it) }

                    if (todoItemsList != null) {
                        for (todoItem in todoItemsList) {
                            cacheStorage.addTodoItem(todoItem)
                        }
                    }

                    val revision = serviceResponse.body()?.revision
                    if (revision != null)
                        setLastKnownRevision(revision)
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

    }

    // Добавление задачи
    suspend fun addTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        cacheStorage.addTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))
        lateinit var response: RepositoryResponse

        val serviceResponse = todoItemService.getService().addTodoItem(
            cacheStorage.getLastKnownRevision(),
            TodoItemContainer(element = TodoItemConverters.todoItemToPojo(todoItem))
        )
        // Если запрос успешный
        if (serviceResponse.isSuccessful) {
            var lastKnownRevision = serviceResponse.body()?.revision

            if (lastKnownRevision != null)
                setLastKnownRevision(lastKnownRevision)
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
    }


    suspend fun deleteTodoItem(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        cacheStorage.deleteTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().delete(TodoItemConverters.modelToRoom(todoItem))

        // Удаление задачи на сервере
        lateinit var response: RepositoryResponse
        // Изменение задачи на сервере
        val serviceResponse = todoItemService.getService().deleteTodoItem(
            todoItem.id,
            cacheStorage.getLastKnownRevision())
        // Если запрос успешный
        if (serviceResponse.isSuccessful) {
            var lastKnownRevision = serviceResponse.body()?.revision

            if (lastKnownRevision != null)
                setLastKnownRevision(lastKnownRevision)
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
    }

    fun getTodoItemsList(): ArrayList<TodoItem> {
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
                TodoItemContainer(element = TodoItemConverters.todoItemToPojo(newItem)))
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null)
                    setLastKnownRevision(lastKnownRevision)
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
        }catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }

    }

    private fun setLastKnownRevision(revision: Int) {
        cacheStorage.setRevision(revision)
       // todoItemsListDatabase.todoItemsListDao().updateTodoItems(RevisionRoom(revision))
    }



}