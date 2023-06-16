package com.monke.yandextodo.model

import java.util.*

data class TodoItem(val id: String, var text: String, var importance: Int,
                    val creationDate: Calendar,
                    var deadlineDate: Calendar? = null
) {
    var modifiedDate: Calendar? = null
    var completed: Boolean = false
}