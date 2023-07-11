package com.monke.yandextodo.utils.notifications

import android.content.Context
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.workers.notificationFeature.NotificationWorker
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

class NotificationScheduler (
    private val context: Context
) {

    // Откладывает уведомление о задаче на время дедлайна
    fun scheduleTodoNotification(todoItem: TodoItem) {
        val timeDiff = todoItem.deadlineDate?.timeInMillis?.minus(
            Calendar.getInstance().timeInMillis)

        if (timeDiff != null) {
            val title = when (todoItem.importance) {
                Importance.NO_IMPORTANCE -> context.getString(R.string.notification_title_default)
                Importance.HIGH -> context.getString(R.string.notification_title_high)
                Importance.LOW -> context.getString(R.string.notification_title_low)
            }

            val data = Data.Builder()
            data.putString(NotificationHelper.TEXT_KEY, todoItem.text)
            data.putInt(NotificationHelper.ID_KEY, todoItem.id.hashCode())
            data.putString(NotificationHelper.TITLE_KEY, title)

            val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(20, TimeUnit.SECONDS)
                .setInputData(data.build())
                .setId(UUID.fromString(todoItem.id))
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                todoItem.id,
                ExistingWorkPolicy.REPLACE,
                notificationWork)
        }
    }

    // Отменяет отложенное уведомление
    fun cancelTodoNotification(workName: String) {
        WorkManager.getInstance(context).cancelUniqueWork(workName)
    }
}