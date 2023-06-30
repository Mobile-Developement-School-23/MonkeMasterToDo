package com.monke.yandextodo.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.localStorage.databases.TodoItemsListDatabase
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemsListRoom
import com.monke.yandextodo.data.networkService.TodoItemService
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.domain.TodoItemList
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
        }
    }

    // Добавление задачи
    suspend fun addTodoItem(todoItem: TodoItem): RepositoryResponse {
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
            response = RepositoryResponse(
                statusCode = serviceResponse.code(),
                message = serviceResponse.message(),
                body = serviceResponse.body()
            )
        }

        return response

    }

    suspend fun deleteTodoItem(todoItem: TodoItem): RepositoryResponse {
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

        return response

    }

    fun getTodoItemsList(): ArrayList<TodoItem> {
        return cacheStorage.getTodoItemsList()
    }

    fun getTodoItemById(id: String): TodoItem? {
        return cacheStorage.getTodoItemById(id)
    }

    suspend fun setTodoItem(newItem: TodoItem): RepositoryResponse {
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
        } else {
            // Обработка ошибок
            response = RepositoryResponse(
                statusCode = serviceResponse.code(),
                message = serviceResponse.message(),
                body = serviceResponse.body()
            )
        }

        return response

    }

    private fun setLastKnownRevision(revision: Int) {
        cacheStorage.setRevision(revision)
        todoItemsListDatabase.todoItemsListDao().updateTodoItems(TodoItemsListRoom(revision))
    }



}