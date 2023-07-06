package com.monke.yandextodo.ioc.modules

import com.monke.yandextodo.data.cacheStorage.TodoItemCacheStorage
import com.monke.yandextodo.ioc.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class CacheStorageModule {

    @AppScope
    @Provides
    fun provideTodoItemCacheStorage(): TodoItemCacheStorage {
        return TodoItemCacheStorage.getInstance()
    }


}