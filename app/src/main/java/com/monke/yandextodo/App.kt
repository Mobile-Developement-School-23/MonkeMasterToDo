package com.monke.yandextodo

import android.app.Application
import androidx.work.*
import com.monke.yandextodo.data.networkService.service.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.utils.workers.NotificationWorker
import com.monke.yandextodo.utils.workers.NotificationWorkerFactory
import com.monke.yandextodo.utils.workers.SynchronizeWorker
import com.monke.yandextodo.utils.workers.SynchronizeWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set

    @Inject
    lateinit var synchronizeWorkerFactory: SynchronizeWorkerFactory

    @Inject
    lateinit var notificationWorkerFactoryAssisted: NotificationWorkerFactory.Factory

    override fun onCreate() {
        applicationComponent.inject(this)

//        val periodicWork = PeriodicWorkRequestBuilder<SynchronizeWorker>(
//            Constants.SYNC_PERIOD, TimeUnit.HOURS)
//            .addTag(Constants.WORK_TAG)
//            .build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            Constants.WORK_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,
//            periodicWork
//        )


        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(
                notificationWorkerFactoryAssisted.create(
                    TodoItem(
                        UUID.randomUUID().toString(),
                        "text",
                        Importance.NO_IMPORTANCE,
                        Calendar.getInstance()
                    ))).build())


        super.onCreate()
    }

}