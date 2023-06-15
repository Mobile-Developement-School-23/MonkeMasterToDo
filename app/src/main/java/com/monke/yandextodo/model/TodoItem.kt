package com.monke.yandextodo.model

import java.util.*

data class TodoItem(val id: String, val text: String, val importance: Int,
                    val completed: Boolean, val creationDate: Calendar
)