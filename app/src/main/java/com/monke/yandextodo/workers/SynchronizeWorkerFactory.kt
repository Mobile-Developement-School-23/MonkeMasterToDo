package com.monke.yandextodo.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import javax.inject.Inject


//class SynchronizeWorkerFactory(private val repository: TodoItemsRepository) : WorkerFactory() {
//
//
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker? = SynchronizeWorker(
//        appContext,
//        workerParameters,
//        repository)
//
//
//}