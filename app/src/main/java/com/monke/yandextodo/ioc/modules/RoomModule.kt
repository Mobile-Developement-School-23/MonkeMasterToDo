package com.monke.yandextodo.ioc.modules

import android.app.Application
import androidx.room.Room
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideTodoItemStorage(application: Application): TodoItemsDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            TodoItemsDatabase::class.java,
            "todo-items-database").allowMainThreadQueries().build()

}