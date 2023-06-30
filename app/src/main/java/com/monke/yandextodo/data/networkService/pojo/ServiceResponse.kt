package com.monke.yandextodo.data.networkService.pojo

data class ServiceResponse (
    var status: String,
    var revision: Int,
    var list: ArrayList<TodoItemPojo>
)