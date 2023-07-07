package com.monke.yandextodo.presentationState

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.repository.RepositoryResponse
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

// View-model для фрагментов с заданиями
class TodoItemViewModel @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {

//    private val _tasksList = MutableLiveData(ArrayList<TodoItem>())
//    val tasksList: LiveData<ArrayList<TodoItem>> = _tasksList

    val _tasksList = todoItemsRepository.getTodoItemsList()
    //val tasksList: StateFlow<ArrayList<TodoItem>> = _tasksList

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
        }
    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        viewModelScope.launch {
            newTodoItem.modifiedDate = Calendar.getInstance()
            val response = todoItemsRepository.setTodoItem(newTodoItem)
            if (response.statusCode != 200)
                errorMessage.value = response.message
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
        }
    }

    fun mergeDataFromServer() {
        viewModelScope.launch {
            todoItemsRepository.mergeFromServer()
        }
    }

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
                "Упс! Нет подключения к интернету! Придется довольствоваться данными с устройства")
            Constants.CODE_REPOSITORY_SUCCESS -> _uiState.value = UiState.Success
        }
    }


}