package com.monke.yandextodo.presentationState.todoFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.utils.notifications.NotificationScheduler
import javax.inject.Inject

class TodoItemViewModelFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository,
    private val notificationScheduler: NotificationScheduler
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoItemViewModel(todoItemsRepository, notificationScheduler) as T
    }


}