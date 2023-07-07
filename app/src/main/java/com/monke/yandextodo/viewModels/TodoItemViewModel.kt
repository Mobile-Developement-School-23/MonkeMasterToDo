package com.monke.yandextodo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

// View-model для фрагментов с заданиями
class TodoItemViewModel @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {

//    private val _tasksList = MutableLiveData(ArrayList<TodoItem>())
//    val tasksList: LiveData<ArrayList<TodoItem>> = _tasksList

    val _tasksList = todoItemsRepository.getTodoItemsList()
    //val tasksList: StateFlow<ArrayList<TodoItem>> = _tasksList

    val errorMessage = MutableLiveData<String>()

    init {
        viewModelScope.launch { todoItemsRepository.fetchData() }

    }

    fun getTodoItem(id: String): TodoItem? {
        return todoItemsRepository.getTodoItemById(id)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            val response = todoItemsRepository.deleteTodoItem(todoItem)
        }
    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        viewModelScope.launch {
            newTodoItem.modifiedDate = Calendar.getInstance()
            todoItemsRepository.setTodoItem(newTodoItem)
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
            CoroutineScope(Dispatchers.IO).launch {
                todoItemsRepository.addTodoItem(todoItem)
            }
        }
    }


}