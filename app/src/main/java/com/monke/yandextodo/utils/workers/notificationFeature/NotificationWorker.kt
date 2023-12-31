package com.monke.yandextodo.utils.workers.notificationFeature

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.monke.yandextodo.R
import com.monke.yandextodo.utils.notifications.NotificationScheduler
import com.monke.yandextodo.utils.notifications.Notificator

// Worker для отправки уведомлений
class NotificationWorker(
    private val context: Context,
    params: WorkerParameters,
    private val notificator: Notificator
) : Worker(context, params) {

    override fun doWork(): Result {

        // Извлечение данных
        var notificationText = inputData.getString(NotificationScheduler.TEXT_KEY)
        val notificationId = inputData.getInt(NotificationScheduler.ID_KEY, 0)
        var notificationTitle = inputData.getString(NotificationScheduler.TITLE_KEY)

        if (notificationText == null)
            notificationText = ""
        if (notificationTitle == null)
            notificationTitle = context.getString(R.string.notification_title_default)

        // Показ уведомления
        notificator.showNotification(notificationText,notificationTitle, notificationId)
        return Result.success()
    }

}