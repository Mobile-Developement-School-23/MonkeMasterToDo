package com.monke.yandextodo.presentation.taskFeature.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monke.yandextodo.data.TodoItemsRepository
import com.monke.yandextodo.domain.Importance
import com.monke.yandextodo.domain.TodoItem
import java.util.Calendar
import java.util.UUID

class TaskViewModel: ViewModel() {

    private val _task = MutableLiveData(ArrayList<TodoItem>())
    val task: LiveData<ArrayList<TodoItem>> = _task

    fun getTodoItem(id: String): TodoItem? {
        return TodoItemsRepository.getTodoItemById(id)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        TodoItemsRepository.deleteTodoItem(todoItem)
    }

    fun saveTodoItem(newTodoItem: TodoItem) {
        TodoItemsRepository.setTodoItem(newTodoItem)
    }

    fun addTodoItem(text: String, deadlineDate: Calendar?, importance: Importance) {
        val todoItem = TodoItem(
            text = text,
            deadlineDate = deadlineDate,
            importance = importance,
            id = UUID.randomUUID().toString(),
            creationDate = Calendar.getInstance()
        )
        TodoItemsRepository.addTodoItem(todoItem)
    }
}