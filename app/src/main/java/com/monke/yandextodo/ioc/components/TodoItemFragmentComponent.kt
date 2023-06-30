package com.monke.yandextodo.ioc.components

import android.app.Application
import com.monke.yandextodo.ioc.modules.CacheStorageModule
import com.monke.yandextodo.ioc.modules.RoomModule
import com.monke.yandextodo.presentation.todoItemFeature.fragments.TodoItemFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    CacheStorageModule::class,
    RoomModule::class
])
interface TodoItemFragmentComponent {

    fun inject(fragment: TodoItemFragment)

    @Component.Builder
    interface Builder {

        fun build(): TodoItemFragmentComponent

        @BindsInstance
        fun application(application: Application): Builder

    }

}