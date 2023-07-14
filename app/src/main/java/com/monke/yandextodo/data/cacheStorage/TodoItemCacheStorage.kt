package com.monke.yandextodo.data.cacheStorage

import androidx.lifecycle.MutableLiveData
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.ArrayList

// Хранение данных задачи в кэше
class TodoItemCacheStorage {

    private var lastKnownRevision: Int = 0
    private var todoItemsList = MutableLiveData<ArrayList<TodoItem>>(ArrayList())

    fun setRevision(revision: Int) {
        lastKnownRevision = revision
    }

    fun getLastKnownRevision() = lastKnownRevision

    fun addTodoItem(todoItem: TodoItem) {
        todoItemsList.value?.add(todoItem)
        todoItemsList.value = todoItemsList.value
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsList.value?.remove(todoItem)
    }

    fun getTodoItemsList(): MutableLiveData<ArrayList<TodoItem>> {
        return todoItemsList
    }

    fun getTodoItemById(id: String): TodoItem? {
        return todoItemsList.value?.find { it.id == id }
    }

    fun setTodoItem(newItem: TodoItem) {
        var list = todoItemsList.value
        if (list != null) {
            for (i in list.indices) {
                if (list[i].id == newItem.id) {
                    list[i] = newItem
                }
            }
        }
    }

    suspend fun setTodoItemsList(list: ArrayList<TodoItem>) {
        todoItemsList.value = list
    }

    fun clearTodoItemsList() {
        todoItemsList.value?.clear()
    }
}