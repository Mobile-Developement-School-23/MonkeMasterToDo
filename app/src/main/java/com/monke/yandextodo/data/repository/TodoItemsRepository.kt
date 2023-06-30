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
        val response = todoItemService.getService().getTodoItemsList()
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
    suspend fun addTodoItem(todoItem: TodoItem) {
        cacheStorage.addTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))
        lateinit var response: RepositoryResponse

        val serviceResponse = todoItemService.getService().addTodoItem(
            cacheStorage.getLastKnownRevision(),
            TodoItemContainer(element = TodoItemConverters.todoItemToPojo(todoItem)))
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

        //return response

    }

    suspend fun deleteTodoItem(todoItem: TodoItem) {
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

    fun getTodoItemsList(): ArrayList<TodoItem> {
        return cacheStorage.getTodoItemsList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return cacheStorage.getTodoItemById(id)
    }

    suspend fun setTodoItem(newItem: TodoItem) {
        cacheStorage.setTodoItem(newItem)
        todoItemsDatabase.todoItemDao().updateTodoItems(TodoItemConverters.modelToRoom(newItem))

        lateinit var response: RepositoryResponse
        // Запрос на изменение задачи на сервере
        val serviceResponse = todoItemService.getService().setTodoItem(
            newItem.id,
            cacheStorage.getLastKnownRevision(),
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

    private suspend fun setLastKnownRevision(revision: Int) {
        cacheStorage.setRevision(revision)
        todoItemsListDatabase.todoItemsListDao().updateTodoItems(TodoItemsListRoom(revision))
    }


}