package com.monke.yandextodo.domain

import java.util.*

data class TodoItem(val id: String,
                    var text: String,
                    var importance: Importance,
                    val creationDate: Calendar,
                    var deadlineDate: Calendar? = null,
                    var modifiedDate: Calendar? = null,
                    var completed: Boolean = false,
                    var color: String? = null,
                    var lastUpdatedBy : String? = null
)