package com.monke.yandextodo.data.localStorage.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.monke.yandextodo.data.localStorage.dao.TodoItemsListDao
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemsListRoom

@Database(entities = [TodoItemsListRoom::class], version = 1)
abstract class TodoItemsListDatabase : RoomDatabase() {

    abstract fun todoItemsListDao(): TodoItemsListDao

}
