package com.monke.yandextodo

import android.app.Application
import android.util.Log
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.data.networkService.pojo.TodoItemPojo
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
        CoroutineScope(Dispatchers.IO).launch {
            todoItemsRepository.fetchData()
        }

        val BASE_URL = "https://beta.mrdekk.ru/todobackend/"


    }

}