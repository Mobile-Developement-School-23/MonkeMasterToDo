package com.monke.yandextodo

import android.app.Application
import androidx.work.DelegatingWorkerFactory
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.workers.SynchronizeWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set


    override fun onCreate() {
       // applicationComponent.inject(this)
                super.onCreate()
    }

}