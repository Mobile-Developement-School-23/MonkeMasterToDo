package com.monke.yandextodo.domain

// Класс с данными о системе
data class TodoItemList(
    var lastKnownRevision: Int,
    var todoItemsList: ArrayList<TodoItem>
)