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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

// View-model для фрагментов с заданиями
class TodoItemViewModel (
    private val todoItemsRepository: TodoItemsRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    val tasksList = todoItemsRepository.getTodoItemsList()

    private val _uiState = MutableLiveData<UiState>()
    var uiState: LiveData<UiState> = _uiState

    val errorMessage = MutableLiveData<String?>()

    private val _newTodoItemText = MutableStateFlow("")
    val newTodoItemText: StateFlow<String> = _newTodoItemText

    private val _newTodoItemImportance = MutableStateFlow<Importance?>(null)
    val newTodoItemImportance: StateFlow<Importance?> = _newTodoItemImportance

    private val _newTodoItemDeadline = MutableStateFlow<Calendar?>(null)
    val newTodoItemDeadline: StateFlow<Calendar?> = _newTodoItemDeadline

    private val _newTodoItemDeadlineTime = MutableStateFlow<Calendar?>(null)
    val newTodoItemDeadlineTime: StateFlow<Calendar?> = _newTodoItemDeadlineTime

    private val _deletingItem = MutableLiveData<TodoItem?>(null)
    val deletingItem: LiveData<TodoItem?> = _deletingItem

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
            _deletingItem.value = todoItem
            if (response.statusCode != 200)
                errorMessage.value = response.message

            notificationScheduler.cancelTodoNotification(todoItem.id)
        }
    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        viewModelScope.launch {
            newTodoItem.modifiedDate = Calendar.getInstance()
            clearNewTodoItemFields()

            val response = todoItemsRepository.setTodoItem(newTodoItem)
            if (response.statusCode != 200)
                errorMessage.value = response.message

            notificationScheduler.cancelTodoNotification(newTodoItem.id)
            if (newTodoItem.deadlineDate != null && !newTodoItem.completed)
                notificationScheduler.scheduleTodoNotification(newTodoItem)
        }
    }

    fun saveTodoItem(
        oldTodoItem: TodoItem,
        text: String,
        deadline: Calendar? = null,
        importance: Importance,
        deadlineTime: Calendar?
    ) {
        var hours = deadlineTime?.get(Calendar.HOUR_OF_DAY)
        if (hours == null)
            hours = 0
        deadline?.set(Calendar.HOUR_OF_DAY, hours)

        var minutes = deadlineTime?.get(Calendar.MINUTE)
        if (minutes == null)
            minutes = 0
        deadline?.set(Calendar.MINUTE, minutes)

        oldTodoItem.text = text
        oldTodoItem.deadlineDate = deadline
        oldTodoItem.importance = importance
        saveTodoItem(oldTodoItem)
    }

    fun addTodoItem(text: String,
                    deadline: Calendar? = null,
                    importance: Importance,
                    deadlineTime: Calendar?
    ) {
        viewModelScope.launch {
            val hours = deadlineTime?.get(Calendar.HOUR_OF_DAY)
            if (hours != null)
                deadline?.set(Calendar.HOUR_OF_DAY, hours)
            val minutes = deadlineTime?.get(Calendar.MINUTE)
            if (minutes != null)
                deadline?.set(Calendar.MINUTE, minutes)

            val todoItem = TodoItem(
                text = text,
                deadlineDate = deadline,
                importance = importance,
                id = UUID.randomUUID().toString(),
                creationDate = Calendar.getInstance(),
                lastUpdatedBy = "no id",
                modifiedDate = Calendar.getInstance(),
            )

            addTodoItem(todoItem)
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
        var list = tasksList.value
        if (list != null) {
            for (todoItem in list) {
                if (todoItem.deadlineDate != null)
                    notificationScheduler.scheduleTodoNotification(todoItem)
            }
        }
    }

    fun setNewTodoItemText(text: String) {
        _newTodoItemText.value = text
    }

    fun setNewTodoItemDeadline(deadline: Calendar?) {
        _newTodoItemDeadline.value = deadline
    }

    fun setNewTodoItemImportance(importance: Importance?) {
        _newTodoItemImportance.value = importance
    }

    fun setNewTodoItemDeadlineTime(deadlineTime: Calendar?) {
        _newTodoItemDeadlineTime.value = deadlineTime
    }

    fun clearNewTodoItemFields() {
        _newTodoItemImportance.value = null
        _newTodoItemDeadline.value = null
        _newTodoItemText.value = ""
        _newTodoItemDeadlineTime.value = null
    }

    fun cancelDelete(todoItem: TodoItem) {
        viewModelScope.launch {
            addTodoItem(todoItem)
            _deletingItem.value = null
        }

    }

    fun confirmDelete() {
        _deletingItem.value = null
    }

    private suspend fun addTodoItem(todoItem: TodoItem) {
        val response = todoItemsRepository.addTodoItem(todoItem)
        if (response.statusCode != 200)
            errorMessage.value = response.message

        if (todoItem.deadlineDate != null)
            notificationScheduler.scheduleTodoNotification(todoItem)
    }



}