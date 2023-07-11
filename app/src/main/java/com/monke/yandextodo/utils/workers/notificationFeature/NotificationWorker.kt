package com.monke.yandextodo.utils.workers.notificationFeature

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.monke.yandextodo.R
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.notifications.NotificationHelper
import com.monke.yandextodo.utils.notifications.Notificator

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters,
    private val notificator: Notificator
) : Worker(context, params) {

    override fun doWork(): Result {

        var notificationText = inputData.getString(NotificationHelper.TEXT_KEY)
        val notificationId = inputData.getInt(NotificationHelper.ID_KEY, 0)
        var notificationTitle = inputData.getString(NotificationHelper.TITLE_KEY)

        if (notificationText == null)
            notificationText = ""

        if (notificationTitle == null)
            notificationTitle = context.getString(R.string.notification_title_default)

        notificator.addNotification(notificationText,notificationTitle, notificationId)
        return Result.success()
    }

}