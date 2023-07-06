package com.monke.yandextodo.data.networkService.api

import com.monke.yandextodo.data.networkService.pojo.ServiceResponse
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TodoItemApi {
    @GET("list")
    suspend fun getTodoItemsList(): Response<ServiceResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Response<ServiceResponse>

    @PUT("list/{todoId}")
    suspend fun setTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Response<ServiceResponse>

    @DELETE("list/{todoId}")
    suspend fun deleteTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int
    ): Response<ServiceResponse>
}