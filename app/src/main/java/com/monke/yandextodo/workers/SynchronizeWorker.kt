package com.monke.yandextodo.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import dagger.assisted.AssistedInject
import javax.inject.Inject

class SynchronizeWorker (
    context: Context,
    params: WorkerParameters,

): CoroutineWorker(context, params) {

    @Inject
    lateinit var repository: TodoItemsRepository

    override suspend fun doWork(): Result {
        val repositoryResponse = repository.synchronizeWithServer()

        if (repositoryResponse.statusCode == Constants.CODE_REPOSITORY_SUCCESS)
            return Result.success()
        return Result.failure()
    }


}