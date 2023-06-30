package com.monke.yandextodo

import android.app.Application
import android.util.Log
import com.monke.yandextodo.data.api.TodoItemContainer
import com.monke.yandextodo.data.api.TodoItemRetrofit
import com.monke.yandextodo.data.api.TodoItemService
import com.monke.yandextodo.data.api.TokenInterceptor
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemFragmentComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemsListFragmentComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import javax.inject.Inject


class App: Application() {

    val todoItemFragmentComponent = DaggerTodoItemFragmentComponent.builder()
        .application(this).build()
    val todoItemsListFragmentComponent = DaggerTodoItemsListFragmentComponent.builder()
        .application(this).build()
    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)
        todoItemsRepository.fetchData()

        val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

        val t = Thread() {

            var client = OkHttpClient.Builder().addInterceptor(TokenInterceptor()).build()

            val retrofit: Retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var service = retrofit.create(TodoItemService::class.java)

            var reponse = service.addTodoItem(
                3,
                TodoItemContainer(TodoItemRetrofit(
                    id = "fdghjkl;1234",
                    text = "ajajajjaa",
                    importance = "low",
                    deadline = 1000,
                    done = false,
                    created_at = 0,
                    changed_at = 0,
                    color = "#FFFFFF",
                    last_updated_by = "hfghghgh"
                )
            )).execute()


            if (reponse.isSuccessful) {
                Log.d("POST ITEM", reponse.body().toString())
            } else {
                Log.d("POST ITEM", reponse.message().toString())
            }

            reponse = service.getTodoItemsList().execute()
            if (reponse.isSuccessful) {
                Log.d("GET LIST", reponse.body().toString())
            } else {
                Log.d("GET LIST", reponse.message().toString())
            }
        }

        t.start()

    }

}