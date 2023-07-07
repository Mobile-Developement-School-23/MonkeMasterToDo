package com.monke.yandextodo.presentationState

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.yandextodo.data.repository.TodoItemsRepository
import javax.inject.Inject

class TodoItemViewModelFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoItemViewModel(todoItemsRepository) as T
    }


}