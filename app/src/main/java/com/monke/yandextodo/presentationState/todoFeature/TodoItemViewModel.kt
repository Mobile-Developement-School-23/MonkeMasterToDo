package com.monke.yandextodo.presentationState.todoFeature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.repository.RepositoryResponse
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import com.monke.yandextodo.utils.notifications.NotificationScheduler
import kotlinx.coroutines.launch
import java.util.*

// View-model для фрагментов с заданиями
class TodoItemViewModel (
    private val todoItemsRepository: TodoItemsRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    val _tasksList = todoItemsRepository.getTodoItemsList()

    private val _uiState = MutableLiveData<UiState>()
    var uiState: LiveData<UiState> = _uiState

    val errorMessage = MutableLiveData<String?>()

    init {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = todoItemsRepository.fetchData()

            updateUiState(result)
        }

    }

    fun getTodoItem(id: String): TodoItem? {
        return todoItemsRepository.getTodoItemById(id)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            val response = todoItemsRepository.deleteTodoItem(todoItem)
            if (response.statusCode != 200)
                errorMessage.value = response.message

            notificationScheduler.cancelTodoNotification(todoItem.id)
        }
    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        viewModelScope.launch {
            newTodoItem.modifiedDate = Calendar.getInstance()
            val response = todoItemsRepository.setTodoItem(newTodoItem)
            if (response.statusCode != 200)
                errorMessage.value = response.message

            notificationScheduler.cancelTodoNotification(newTodoItem.id)
            if (newTodoItem.deadlineDate != null)
                notificationScheduler.scheduleTodoNotification(newTodoItem)
        }
    }

    fun addTodoItem(text: String, deadlineDate: Calendar?, importance: Importance) {
        viewModelScope.launch {
            val todoItem = TodoItem(
                text = text,
                deadlineDate = deadlineDate,
                importance = importance,
                id = UUID.randomUUID().toString(),
                creationDate = Calendar.getInstance(),
                lastUpdatedBy = "no id",
                modifiedDate = Calendar.getInstance(),
            )
            val response = todoItemsRepository.addTodoItem(todoItem)
            if (response.statusCode != 200)
                errorMessage.value = response.message

            if (todoItem.deadlineDate != null)
                notificationScheduler.scheduleTodoNotification(todoItem)

        }
    }

    // Загрузка данных с сервера
    fun mergeDataFromServer() {
        viewModelScope.launch {
            todoItemsRepository.mergeFromServer()

            // Добавление отложенных уведомлений для загруженных задач
            scheduleNotifications()
        }
    }

    // Загрузка данных с локального хранилища
    fun mergeDataFromDatabase() {
        viewModelScope.launch {
            val result = todoItemsRepository.mergeFromDatabase()

            updateUiState(result)
        }
    }

    private fun updateUiState(result: RepositoryResponse) {
        when (result.statusCode) {
            Constants.CODE_NEED_SYNC -> _uiState.value = UiState.NeedSync
            Constants.CODE_NO_NETWORK -> _uiState.value = UiState.Error(
                "Упс! Нет подключения к интернету! Придется довольствоваться данными с устройства"
            )
            Constants.CODE_REPOSITORY_SUCCESS -> _uiState.value = UiState.Success
        }
    }

    private fun scheduleNotifications() {
        for (todoItem in _tasksList.value) {
            if (todoItem.deadlineDate != null)
                notificationScheduler.scheduleTodoNotification(todoItem)
        }
    }



}