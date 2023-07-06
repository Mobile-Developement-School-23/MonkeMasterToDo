package com.monke.yandextodo.domain

// Класс-список задач
// Содержит список задач и его ревизию
data class TodoItemList(
    var lastKnownRevision: Int,
    var todoItemsList: ArrayList<TodoItem>
)