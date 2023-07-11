package com.monke.yandextodo

import android.app.Application
import androidx.work.*
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.utils.notifications.NotificationHelper
import com.monke.yandextodo.utils.workers.TodoWorkerFactory
import com.monke.yandextodo.utils.workers.notificationFeature.NotificationWorker
import com.monke.yandextodo.utils.workers.notificationFeature.NotificationWorkerFactory
import com.monke.yandextodo.utils.workers.syncFeature.SynchronizeWorkerFactory
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set

    @Inject
    lateinit var todoWorkerFactory: TodoWorkerFactory

    override fun onCreate() {

        applicationComponent.inject(this)

        // Инициализация Work Manager
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(todoWorkerFactory)
                .build())

        super.onCreate()
    }

}