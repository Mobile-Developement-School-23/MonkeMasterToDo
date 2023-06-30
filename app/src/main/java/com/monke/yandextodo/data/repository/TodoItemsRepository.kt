package com.monke.yandextodo.data.repository

import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.domain.TodoItem
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class TodoItemsRepository @Inject constructor(
    private val cacheStorage: TodoItemCacheStorage,
    private val todoItemsDatabase: TodoItemsDatabase
) {

    fun fetchData() {
        val todoItemsRoom = todoItemsDatabase.todoItemDao().getTodoItemsList()
        val todoItems = ArrayList<TodoItem>()

        for (todoItemRoom in todoItemsRoom) {
            todoItems.add(TodoItemConverters.roomToModel(todoItemRoom))
        }

        cacheStorage.addAll(todoItems)
    }


    fun addTodoItem(todoItem: TodoItem) {
        cacheStorage.addTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().addTodoItems(TodoItemConverters.modelToRoom(todoItem))
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        cacheStorage.deleteTodoItem(todoItem)
        todoItemsDatabase.todoItemDao().delete(TodoItemConverters.modelToRoom(todoItem))
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
    }


}