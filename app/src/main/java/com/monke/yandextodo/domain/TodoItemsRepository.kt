package com.monke.yandextodo.domain

import com.monke.yandextodo.model.TodoItem

object TodoItemsRepository {

    var todoItemsList: ArrayList<TodoItem> = ArrayList()
        private set

    fun addTodoItem(todoItem: TodoItem) {
        todoItemsList.add(todoItem)
    }


}