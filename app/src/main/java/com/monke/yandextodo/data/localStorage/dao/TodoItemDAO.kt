package com.monke.yandextodo.data.localStorage.dao

import androidx.room.*
import com.monke.yandextodo.data.localStorage.roomModels.TodoItemRoom
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoItemDAO {

    @Query("SELECT * from todoItemRoom")
    fun getTodoItemsList(): Flow<List<TodoItemRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodoItems(vararg todoItems: TodoItemRoom)

    @Delete
    fun delete(todoItem: TodoItemRoom)

    @Update
    fun updateTodoItems(vararg todoItems: TodoItemRoom)



}