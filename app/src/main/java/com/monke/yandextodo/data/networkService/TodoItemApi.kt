package com.monke.yandextodo.data.networkService

import com.monke.yandextodo.data.networkService.pojo.Response
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import retrofit2.Call
import retrofit2.http.*

interface TodoItemApi {
    @GET("list")
    fun getTodoItemsList(): Call<Response>

    @POST("list")
    fun addTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Call<Response>

    @PUT("list/{todoId}")
    fun setTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer
    ): Call<Response>

    @DELETE("list/{todoId}")
    fun deleteTodoItem(
        @Path("todoId") todoId: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int
    ): Call<Response>
}