package com.monke.yandextodo.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import javax.inject.Inject

class SynchronizeWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repository: TodoItemsRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repositoryResponse = repository.synchronizeWithServer()

        if (repositoryResponse.statusCode == Constants.CODE_REPOSITORY_SUCCESS)
            return Result.success()
        return Result.failure()
    }


}