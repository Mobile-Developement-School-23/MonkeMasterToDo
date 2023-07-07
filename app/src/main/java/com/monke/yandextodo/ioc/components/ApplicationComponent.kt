package com.monke.yandextodo.ioc.components

import android.app.Application
import com.monke.yandextodo.App
import com.monke.yandextodo.ioc.modules.CacheStorageModule
import com.monke.yandextodo.ioc.modules.NetworkModule
import com.monke.yandextodo.ioc.modules.RoomModule
import com.monke.yandextodo.ioc.scopes.AppScope
import com.monke.yandextodo.presentation.MainTodoActivity
import dagger.BindsInstance
import dagger.Component


@AppScope
@Component(modules = [
    CacheStorageModule::class,
    RoomModule::class,
    NetworkModule::class
])
interface ApplicationComponent {

    fun mainTodoActivityComponent(): MainTodoActivityComponent

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder

    }


}

