package com.monke.yandextodo.presentation.taskFeature.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.TodoItemsRepository
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.launch

class TasksListViewModel: ViewModel() {

    private val _tasksList = MutableLiveData(ArrayList<TodoItem>())
    val tasksList: LiveData<ArrayList<TodoItem>> = _tasksList

    init {
        viewModelScope.launch {
            _tasksList.value = TodoItemsRepository.getTodoItemsList()
        }
    }


}