package com.monke.yandextodo.data.api

import retrofit2.Call
import retrofit2.http.*

interface TodoItemService {
    @GET("list")
    fun getTodoItemsList(): Call<Response>


    @POST("list")
    fun addTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body todoItem: TodoItemContainer): Call<Response>
}