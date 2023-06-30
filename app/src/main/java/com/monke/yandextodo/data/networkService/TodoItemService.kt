package com.monke.yandextodo.data.networkService

import retrofit2.Retrofit
import javax.inject.Inject

class TodoItemService @Inject constructor(
    val retrofitClient: Retrofit
) {

    fun getService() = retrofitClient.create(TodoItemApi::class.java)

}