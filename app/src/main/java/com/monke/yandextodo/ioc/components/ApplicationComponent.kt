package com.monke.yandextodo.ioc.components

import android.app.Application
import com.monke.yandextodo.App
import com.monke.yandextodo.ioc.modules.CacheStorageModule
import com.monke.yandextodo.ioc.modules.NetworkModule
import com.monke.yandextodo.ioc.modules.NotificatorModule
import com.monke.yandextodo.ioc.modules.RoomModule
import com.monke.yandextodo.ioc.scopes.AppScope
import dagger.BindsInstance
import dagger.Component


@AppScope
@Component(modules = [
    CacheStorageModule::class,
    RoomModule::class,
    NetworkModule::class,
    NotificatorModule::class
])
interface ApplicationComponent {

    fun mainTodoActivityComponent(): MainTodoActivityComponent

    fun inject(app: App)

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: Application): Builder

    }


}

