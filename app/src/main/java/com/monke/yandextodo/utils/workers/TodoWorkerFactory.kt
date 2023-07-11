package com.monke.yandextodo.utils.workers

import androidx.work.DelegatingWorkerFactory
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.utils.notifications.Notificator
import com.monke.yandextodo.utils.workers.notificationFeature.NotificationWorkerFactory
import com.monke.yandextodo.utils.workers.syncFeature.SynchronizeWorkerFactory
import javax.inject.Inject

class TodoWorkerFactory @Inject constructor(
    todoItemsRepository: TodoItemsRepository,
    notificator: Notificator
): DelegatingWorkerFactory() {

    init {
        addFactory(NotificationWorkerFactory(notificator))
        addFactory(SynchronizeWorkerFactory(todoItemsRepository))
    }

}