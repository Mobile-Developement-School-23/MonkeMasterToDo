package com.monke.yandextodo.utils.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.notifications.Notificator

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val todoItem: TodoItem,
    private val notificator: Notificator
) : Worker(context, params) {

    override fun doWork(): Result {
        notificator.addNotification(todoItem.text, todoItem.id.hashCode())
        return Result.success()
    }

}