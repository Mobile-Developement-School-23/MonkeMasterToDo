package com.monke.yandextodo.data.cacheStorage

import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import java.util.*
import kotlin.collections.ArrayList

// Хранение данных задачи в кэше
class TodoItemCacheStorage private constructor() {

    private var todoItemsList: ArrayList<TodoItem> = ArrayList()

    companion object {

        private var instance: TodoItemCacheStorage? = null

        fun getInstance(): TodoItemCacheStorage {
            if (instance == null) {
                instance = TodoItemCacheStorage()
            }
            return instance!!
        }
    }

//    init {
//        // Мокнутый репозиторий
//        for (i in 1..20) {
//            if (i % 2 == 0)
//                addTodoItem(
//                    TodoItem(
//                        i.toString(), Constants.MOCK_BIG_TASK_TEXT,
//                        if (i % 3 == 0) Importance.NO_IMPORTANCE
//                        else if (i % 3 == 1) Importance.LOW
//                        else Importance.HIGH,
//                        Calendar.getInstance(), deadlineDate = Calendar.getInstance())
//                )
//            else
//                addTodoItem(
//                    TodoItem(
//                        i.toString(),
//                        "Поступить в ШМР",
//                        if (i % 3 == 0) Importance.NO_IMPORTANCE
//                        else if (i % 3 == 1) Importance.LOW
//                        else Importance.HIGH,
//                        Calendar.getInstance())
//                )
//        }
//    }

    fun addTodoItem(todoItem: TodoItem) {
        todoItemsList.add(todoItem)
    }

    fun addAll(todoItem: List<TodoItem>) {
        todoItemsList.addAll(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        todoItemsList.remove(todoItem)
    }

    fun getTodoItemsList(): ArrayList<TodoItem> {
        return todoItemsList
    }

    fun getTodoItemById(id: String): TodoItem? {
        return todoItemsList.find { it.id == id }
    }

    fun setTodoItem(newItem: TodoItem) {
        for (i in todoItemsList.indices) {
            if (todoItemsList[i].id == newItem.id) {
                todoItemsList[i] = newItem
            }
        }
    }
}