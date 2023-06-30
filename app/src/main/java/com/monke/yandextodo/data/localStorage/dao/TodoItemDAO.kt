package com.monke.yandextodo.data.localStorage.dao

import androidx.room.*
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom

@Dao
interface TodoItemDAO {

    @Query("SELECT * from todoItemRoom")
    fun getTodoItemsList(): List<TodoItemRoom>

    @Insert
    fun addTodoItems(vararg todoItems: TodoItemRoom)

    @Delete
    fun delete(todoItem: TodoItemRoom)

    @Update
    fun updateTodoItems(vararg todoItems: TodoItemRoom)

}