package com.monke.yandextodo.data.localStorage.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.monke.yandextodo.data.localStorage.dao.RevisionDao
import com.monke.yandextodo.data.localStorage.dao.TodoItemDAO
import com.monke.yandextodo.data.localStorage.roomModels.RevisionRoom
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom

@Database(
    entities = [TodoItemRoom::class, RevisionRoom::class],
    version = 1
)
abstract class TodoItemsDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDAO
    abstract fun revisionDao(): RevisionDao

}