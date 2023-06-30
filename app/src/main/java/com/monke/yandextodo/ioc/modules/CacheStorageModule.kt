package com.monke.yandextodo.ioc.modules

import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import dagger.Module
import dagger.Provides

@Module
object CacheStorageModule {

    @Provides
    fun provideTodoItemCacheStorage(): TodoItemCacheStorage {
        return TodoItemCacheStorage.getInstance()
    }


}