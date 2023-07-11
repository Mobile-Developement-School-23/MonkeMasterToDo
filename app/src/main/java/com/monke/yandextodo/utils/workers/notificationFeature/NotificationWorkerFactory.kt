package com.monke.yandextodo.utils.workers.notificationFeature

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.monke.yandextodo.utils.notifications.Notificator
import javax.inject.Inject

class NotificationWorkerFactory(
    private val notificator: Notificator
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {
            NotificationWorker::class.java.name ->
                NotificationWorker(
                    appContext,
                    workerParameters,
                    notificator)
            else -> null
        }
    }



}