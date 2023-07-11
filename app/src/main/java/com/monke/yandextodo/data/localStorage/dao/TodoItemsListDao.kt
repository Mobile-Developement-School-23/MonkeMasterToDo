package com.monke.yandextodo.data.localStorage.dao

import androidx.room.*
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemsListRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemsListDao {

    @Query("SELECT * from todoItemsListRoom")
    fun getTodoItemsList(): Flow<TodoItemsListRoom>

    @Insert
     fun addTodoItemsList(vararg todoItems: TodoItemsListRoom)

    @Delete
     fun delete(todoItem: TodoItemsListRoom)

    @Update
     fun updateTodoItems(vararg todoItems: TodoItemsListRoom)
}