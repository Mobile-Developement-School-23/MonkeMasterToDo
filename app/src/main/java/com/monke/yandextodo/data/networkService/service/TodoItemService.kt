package com.monke.yandextodo.data.networkService.service

import com.monke.yandextodo.data.networkService.api.TodoItemApi
import retrofit2.Retrofit
import javax.inject.Inject

class TodoItemService @Inject constructor(
    private val retrofitClient: Retrofit
) {

    fun getService() = retrofitClient.create(TodoItemApi::class.java)

}