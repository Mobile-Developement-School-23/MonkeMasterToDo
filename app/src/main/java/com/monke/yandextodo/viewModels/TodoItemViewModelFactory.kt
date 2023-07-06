package com.monke.yandextodo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.scopes.MainTodoActivityScope
import javax.inject.Inject

class TodoItemViewModelFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoItemViewModel(todoItemsRepository) as T
    }


}