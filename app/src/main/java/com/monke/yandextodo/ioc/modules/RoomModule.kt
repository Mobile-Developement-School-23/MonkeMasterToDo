package com.monke.yandextodo.ioc.modules

import android.app.Application
import androidx.room.Room
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.data.localStorage.databases.TodoItemsListDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideTodoItemDatabase(application: Application): TodoItemsDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            TodoItemsDatabase::class.java,
            "todo-items-database").allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideTodoItemsListDatabase(application: Application): TodoItemsListDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            TodoItemsListDatabase::class.java,
            "todo-items-list-database").allowMainThreadQueries().build()

}