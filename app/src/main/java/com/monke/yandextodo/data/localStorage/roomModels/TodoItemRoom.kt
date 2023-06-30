package com.monke.yandextodo.data.localStorage.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monke.yandextodo.domain.Importance

@Entity
data class TodoItemRoom(
    @PrimaryKey
    val id: String,
    val text: String,
    val importance: Importance,
    val creationDate: Long,
    val deadlineDate: Long?,
    val modifiedDate: Long?,
    val completed: Boolean
) {


}