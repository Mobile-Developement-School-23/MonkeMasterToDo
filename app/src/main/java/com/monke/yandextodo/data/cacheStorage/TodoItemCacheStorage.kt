package com.monke.yandextodo.data.cacheStorage

import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.ArrayList

// Хранение данных задачи в кэше
// реализует паттерн singletone
class TodoItemCacheStorage private constructor() {

    private var lastKnownRevision: Int = 0
    private var todoItemsList = MutableStateFlow<ArrayList<TodoItem>>(ArrayList())

    companion object {

        private var instance: TodoItemCacheStorage? = null

        fun getInstance(): TodoItemCacheStorage {
            if (instance == null) {
                instance = TodoItemCacheStorage()
            }
            return instance!!
        }
    }

    fun setRevision(revision: Int) {
        lastKnownRevision = revision
    }

    fun getLastKnownRevision() = lastKnownRevision

    fun addTodoItem(todoItem: TodoItem) {
        todoItemsList.value.add(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsList.value.remove(todoItem)
    }

    fun getTodoItemsList(): MutableStateFlow<ArrayList<TodoItem>> {
        return todoItemsList
    }

    fun getTodoItemById(id: String): TodoItem? {
        return todoItemsList.value.find { it.id == id }
    }

    fun setTodoItem(newItem: TodoItem) {
        for (i in todoItemsList.value.indices) {
            if (todoItemsList.value[i].id == newItem.id) {
                todoItemsList.value[i] = newItem
            }
        }
    }

    suspend fun setTodoItemsList(list: ArrayList<TodoItem>) {
        todoItemsList.emit(list)
    }

    fun clearTodoItemsList() {
        todoItemsList.value.clear()
    }
}