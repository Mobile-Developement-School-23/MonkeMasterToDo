package com.monke.yandextodo.data.networkService.pojo


data class TodoItemsContainer (
    var status: String,
    var revision: Int,
    var list: List<TodoItemPojo>
)