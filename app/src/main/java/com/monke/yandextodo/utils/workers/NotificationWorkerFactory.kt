package com.monke.yandextodo.utils.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.notifications.Notificator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class NotificationWorkerFactory @AssistedInject constructor(
    @Assisted("todoItem") private val todoItem: TodoItem,
    private val notificator: Notificator
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return NotificationWorker(
            appContext,
            workerParameters,
            todoItem,
            notificator
        )
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("todoItem") todoItem: TodoItem): NotificationWorkerFactory
    }


}