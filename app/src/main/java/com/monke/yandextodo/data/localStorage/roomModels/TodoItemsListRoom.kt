package com.monke.yandextodo.data.localStorage.roomModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItemsListRoom (
    @PrimaryKey
    @ColumnInfo(name = "revision")
    var revision: Int
)