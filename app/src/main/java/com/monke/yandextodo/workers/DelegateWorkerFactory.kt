package com.monke.yandextodo.workers

import androidx.work.DelegatingWorkerFactory
import com.monke.yandextodo.data.repository.TodoItemsRepository
import javax.inject.Inject


//class DelegateWorkerFactory @Inject constructor(
//    todoItemsRepository: TodoItemsRepository
//) : DelegatingWorkerFactory() {
//
//    init {
//        addFactory(SynchronizeWorkerFactory(todoItemsRepository))
//    }
//}