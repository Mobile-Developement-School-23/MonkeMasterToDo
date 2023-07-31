package com.monke.yandextodo.data.networkService.api

import com.monke.yandextodo.data.networkService.pojo.TodoItemsContainer
import com.monke.yandextodo.data.networkService.pojo.TodoItemElement
import com.monke.yandextodo.data.networkService.pojo.TodoItemsList
import retrofit2.http.*

interface TodoItemApi {
    @GET("list")
    suspend fun getTodoItemsList(): Result<TodoItemsContainer>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemElement
    ): Result<TodoItemsContainer>

    @PUT("list/{todoId}")
    suspend fun setTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemElement
    ): Result<TodoItemsContainer>

    @DELETE("list/{todoId}")
    suspend fun deleteTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int
    ): Result<TodoItemsContainer>

    @PATCH("list")
    suspend fun patchTodoItemsList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemsList
    ): Result<TodoItemsContainer>
}