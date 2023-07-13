package com.monke.yandextodo.ioc.modules

import android.app.Application
import androidx.room.Room
import com.monke.yandextodo.data.localStorage.databases.SettingsDatabase
import com.monke.yandextodo.data.localStorage.databases.TodoItemsDatabase
import com.monke.yandextodo.ioc.scopes.AppScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @AppScope
    @Provides
    fun provideTodoItemDatabase(application: Application): TodoItemsDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            TodoItemsDatabase::class.java,
            "todo-items-database").build()

    @AppScope
    @Provides
    fun provideSettingsDatabase(application: Application): SettingsDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            SettingsDatabase::class.java,
            "settings-database").build()

}