package com.monke.yandextodo.data.cacheStorage

import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.domain.TodoItemList
import kotlin.collections.ArrayList

// Хранение данных задачи в кэше
// реализует паттерн singletone
class TodoItemCacheStorage private constructor() {

    private var todoItemsList = TodoItemList(0, ArrayList())

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
        todoItemsList.lastKnownRevision = revision
    }

    fun getLastKnownRevision() = todoItemsList.lastKnownRevision

    fun addTodoItem(todoItem: TodoItem) {
        todoItemsList.todoItemsList.add(todoItem)
    }

    fun addAll(todoItem: List<TodoItem>) {
        todoItemsList.todoItemsList.addAll(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsList.todoItemsList.remove(todoItem)
    }

    fun getTodoItemsList(): ArrayList<TodoItem> {
        return todoItemsList.todoItemsList
    }

    fun getTodoItemById(id: String): TodoItem? {
        return todoItemsList.todoItemsList.find { it.id == id }
    }

    fun setTodoItem(newItem: TodoItem) {
        for (i in todoItemsList.todoItemsList.indices) {
            if (todoItemsList.todoItemsList[i].id == newItem.id) {
                todoItemsList.todoItemsList[i] = newItem
            }
        }
    }
}