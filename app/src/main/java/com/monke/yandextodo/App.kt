package com.monke.yandextodo

import android.app.Application
import android.util.Log
import com.monke.yandextodo.data.networkService.pojo.TodoItemContainer
import com.monke.yandextodo.data.networkService.pojo.TodoItemPojo
import com.monke.yandextodo.data.networkService.TodoItemService
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemFragmentComponent
import com.monke.yandextodo.ioc.components.DaggerTodoItemsListFragmentComponent
import javax.inject.Inject


class App: Application() {

    val todoItemFragmentComponent = DaggerTodoItemFragmentComponent.builder()
        .application(this).build()
    val todoItemsListFragmentComponent = DaggerTodoItemsListFragmentComponent.builder()
        .application(this).build()
    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository
    @Inject
    lateinit var todoItemService: TodoItemService

    override fun onCreate() {
        super.onCreate()

        applicationComponent.inject(this)
        todoItemsRepository.fetchData()

        val BASE_URL = "https://beta.mrdekk.ru/todobackend/"

//        val t = Thread() {
//            var reponse = todoItemService.getService().addTodoItem(
//                3,
//                TodoItemContainer(
//                    TodoItemPojo(
//                    id = "fdghjkl;1234",
//                    text = "ajajajjaa",
//                    importance = "low",
//                    deadline = 1000,
//                    done = false,
//                    created_at = 0,
//                    changed_at = 0,
//                    color = "#FFFFFF",
//                    last_updated_by = "hfghghgh"
//                )
//            )
//            ).execute()
//
//
//            if (reponse.isSuccessful) {
//                Log.d("POST ITEM", reponse.body().toString())
//            } else {
//                Log.d("POST ITEM", reponse.message().toString())
//            }
//
//            reponse = todoItemService.getService().getTodoItemsList().execute()
//            if (reponse.isSuccessful) {
//                Log.d("GET LIST", reponse.body().toString())
//            } else {
//                Log.d("GET LIST", reponse.message().toString())
//            }
//        }
//
//        t.start()

    }

}