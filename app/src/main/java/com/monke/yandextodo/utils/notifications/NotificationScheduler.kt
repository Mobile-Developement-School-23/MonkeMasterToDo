package com.monke.yandextodo.utils.notifications

import android.content.Context
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

// Класс для отправки уведомлений в дату дедлайна
class NotificationScheduler (
    private val context: Context
) {

    companion object {
        const val TEXT_KEY = "notification text"
        const val ID_KEY = "notification id"
        const val TITLE_KEY = "notification title"
    }

    // Откладывает уведомление о задаче на время дедлайна
    fun scheduleTodoNotification(todoItem: TodoItem) {
        val timeDiff = todoItem.deadlineDate?.timeInMillis?.minus(
            Calendar.getInstance().timeInMillis)

        // Если задание уже просрочено, то уведомление не покажется
        if (timeDiff == null || timeDiff < 0)
            return

        val title = when (todoItem.importance) {
            Importance.NO_IMPORTANCE -> context.getString(R.string.notification_title_default)
            Importance.HIGH -> context.getString(R.string.notification_title_high)
            Importance.LOW -> context.getString(R.string.notification_title_low)
        }

        // Данные для уведомления
        val data = Data.Builder()
        data.putString(TEXT_KEY, todoItem.text)
        data.putInt(ID_KEY, todoItem.id.hashCode())
        data.putString(TITLE_KEY, title)

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

    // Отменяет отложенное уведомление
    fun cancelTodoNotification(workName: String) {
        WorkManager.getInstance(context).cancelUniqueWork(workName)
    }
}