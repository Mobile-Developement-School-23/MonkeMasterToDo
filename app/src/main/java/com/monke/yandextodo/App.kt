package com.monke.yandextodo

import android.app.Application
import androidx.work.*
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.workers.SynchronizeWorker
import com.monke.yandextodo.workers.SynchronizeWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set

    @Inject lateinit var synchronizeWorkerFactory: SynchronizeWorkerFactory

    override fun onCreate() {
        applicationComponent.inject(this)
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(synchronizeWorkerFactory).build())

        val periodicWork = PeriodicWorkRequestBuilder<SynchronizeWorker>(
            Constants.SYNC_PERIOD, TimeUnit.HOURS)
            .addTag(Constants.WORK_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )
        super.onCreate()
    }

}