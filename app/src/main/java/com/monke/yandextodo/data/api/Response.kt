package com.monke.yandextodo.data.api

import com.monke.yandextodo.domain.TodoItem

data class Response (
    var status: String,
    var revision: Int,
    var list: ArrayList<TodoItemRetrofit>
)