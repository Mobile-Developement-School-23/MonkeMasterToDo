package com.monke.yandextodo.presentation.todoItemFeature.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TodoItemViewModel @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository
) : ViewModel() {

    private val _tasksList = MutableLiveData(ArrayList<TodoItem>())
    val tasksList: LiveData<ArrayList<TodoItem>> = _tasksList

    init {
        viewModelScope.launch {
            _tasksList.value = todoItemsRepository.getTodoItemsList()
        }
    }


    fun getTodoItem(id: String): TodoItem? {
        return todoItemsRepository.getTodoItemById(id)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            todoItemsRepository.deleteTodoItem(todoItem)
        }

    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            todoItemsRepository.setTodoItem(newTodoItem)
        }
    }

    fun addTodoItem(text: String, deadlineDate: Calendar?, importance: Importance) {
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