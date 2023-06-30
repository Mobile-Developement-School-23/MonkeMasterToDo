package com.monke.yandextodo

import android.app.Application
import com.monke.yandextodo.data.networkService.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemFragmentComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemsListFragmentComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class App: Application() {

    val todoItemFragmentComponent = DaggerTodoItemFragmentComponent.builder()
        .application(this).build()
    val todoItemsListFragmentComponent = DaggerTodoItemsListFragmentComponent.builder()
        .application(this).build()
    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository
    @Inject
    lateinit var todoItemService: TodoItemService

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)

        // Загрузка данных в кэш
        CoroutineScope(Dispatchers.IO).launch {
            todoItemsRepository.fetchData()
        }


    }

}