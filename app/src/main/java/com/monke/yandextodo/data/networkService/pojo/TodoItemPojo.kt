package com.monke.yandextodo.data.networkService.pojo

data class TodoItemPojo (
    var id: String,
    var text: String,
    var importance: String,
    var deadline: Long? = null,
    var done: Boolean,
    var color: String? = null,
    var created_at: Long,
    var changed_at: Long? = null,
    var last_updated_by: String? = null
)