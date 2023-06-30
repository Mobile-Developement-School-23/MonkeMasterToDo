package com.monke.yandextodo.data.repository

import android.util.Log
import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.localStorage.databases.TodoItemsListDatabase
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemsListRoom
import com.monke.yandextodo.data.networkService.TodoItemService
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.domain.TodoItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class TodoItemsRepository @Inject constructor(
    private val cacheStorage: TodoItemCacheStorage,
    private val todoItemsDatabase: TodoItemsDatabase,
    private val todoItemService: TodoItemService,
    private val todoItemsListDatabase: TodoItemsListDatabase
) {

    fun fetchData() {
        var t = Thread() {
            fetchDataFromServer()
        }

        t.start()
    }

    private fun fetchDataDatabase() {
        val todoItemsRoom = todoItemsDatabase.todoItemDao().getTodoItemsList()
        val todoItems = ArrayList<TodoItem>()

        for (todoItemRoom in todoItemsRoom) {
            todoItems.add(TodoItemConverters.roomToModel(todoItemRoom))
        }

        cacheStorage.addAll(todoItems)
    }

    private fun fetchDataFromServer() {
        val response = todoItemService.getService().getTodoItemsList().execute()
        if (response.isSuccessful) {
            var todoItemsList = response.body()?.list?.map { TodoItemConverters.todoItemFromPojo(it) }

            if (todoItemsList != null) {
                for (todoItem in todoItemsList) {
                    cacheStorage.addTodoItem(todoItem)
                }
            }

            val revision = response.body()?.revision
            if (revision != null)
                setLastKnownRevision(revision)
        } //else
            // Обработка ошибок
    }

    // Добавление задачи
    fun addTodoItem(todoItem: TodoItem) {
        cacheStorage.addTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))
        lateinit var response: RepositoryResponse

        var t = Thread() {
            // Запрос на добавление задачи на сервер
            val serviceResponse = todoItemService.getService().addTodoItem(
                cacheStorage.getLastKnownRevision(),
                TodoItemContainer(element = TodoItemConverters.todoItemToPojo(todoItem))).execute()
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
                Log.d("ERROR ", serviceResponse.message())
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }
        }

        t.start()

        //return response

    }

    fun deleteTodoItem(todoItem: TodoItem) {
        cacheStorage.deleteTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().delete(TodoItemConverters.modelToRoom(todoItem))

        // Удаление задачи на сервере
        lateinit var response: RepositoryResponse
        // Изменение задачи на сервере
        var t = Thread() {
            // Запрос на изменение задачи на сервере
            val serviceResponse = todoItemService.getService().deleteTodoItem(
                todoItem.id,
                cacheStorage.getLastKnownRevision()).execute()
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null)
                    setLastKnownRevision(lastKnownRevision)
                response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success"
                )
                Log.d("SUCCESS", todoItem.text)
            } else {
                // Обработка ошибок
                Log.d("ERROR ", serviceResponse.errorBody().toString())
                Log.d("ERROR ", todoItem.text)
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }
        }

        t.start()
    }

    fun getTodoItemsList(): ArrayList<TodoItem> {
        return cacheStorage.getTodoItemsList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return cacheStorage.getTodoItemById(id)
    }

    fun setTodoItem(newItem: TodoItem) {
        cacheStorage.setTodoItem(newItem)
        todoItemsDatabase.todoItemDao().updateTodoItems(TodoItemConverters.modelToRoom(newItem))

        lateinit var response: RepositoryResponse
        // Изменение задачи на сервере
        var t = Thread() {
            // Запрос на изменение задачи на сервере
            val serviceResponse = todoItemService.getService().setTodoItem(
                newItem.id,
                cacheStorage.getLastKnownRevision(),
                TodoItemContainer(element = TodoItemConverters.todoItemToPojo(newItem))).execute()
            // Если запрос успешный
            if (serviceResponse.isSuccessful) {
                var lastKnownRevision = serviceResponse.body()?.revision

                if (lastKnownRevision != null)
                    setLastKnownRevision(lastKnownRevision)
                response = RepositoryResponse(
                    statusCode = 200,
                    message = "Success"
                )
                Log.d("SUCCESS", newItem.text)
            } else {
                // Обработка ошибок
                Log.d("ERROR ", serviceResponse.errorBody().toString())
                Log.d("ERROR ", newItem.text)
                response = RepositoryResponse(
                    statusCode = serviceResponse.code(),
                    message = serviceResponse.message(),
                    body = serviceResponse.body()
                )
            }
        }

        t.start()
    }

    fun setLastKnownRevision(revision: Int) {
        cacheStorage.setRevision(revision)
        todoItemsListDatabase.todoItemsListDao().updateTodoItems(TodoItemsListRoom(revision))
    }


}