package com.monke.yandextodo.data.localStorage.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.monke.yandextodo.data.converters.TodoItemConverters
import com.monke.yandextodo.data.localStorage.dao.TodoItemDAO
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom

@Database(entities = [TodoItemRoom::class], version = 1)
abstract class TodoItemsDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDAO

}