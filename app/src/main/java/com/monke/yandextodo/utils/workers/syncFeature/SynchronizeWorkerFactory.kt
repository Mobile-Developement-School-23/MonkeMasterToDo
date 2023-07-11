package com.monke.yandextodo.utils.workers.syncFeature

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import javax.inject.Inject

class SynchronizeWorkerFactory (
    private val todoItemsRepository: TodoItemsRepository
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SynchronizeWorker::class.java.name ->
                SynchronizeWorker(
                    appContext,
                    workerParameters,
                    todoItemsRepository)
            else -> null
        }
    }


}