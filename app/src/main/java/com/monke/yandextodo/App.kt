package com.monke.yandextodo

import android.app.Application
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)

        // Загрузка данных в кэш
        CoroutineScope(Dispatchers.IO).launch {
            todoItemsRepository.fetchData()
        }


    }

}