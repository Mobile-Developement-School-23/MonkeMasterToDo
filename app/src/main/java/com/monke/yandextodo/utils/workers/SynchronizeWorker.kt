package com.monke.yandextodo.utils.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.monke.yandextodo.data.repository.TodoItemsRepository
import com.monke.yandextodo.domain.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

class SynchronizeWorker (
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

    class Factory @Inject constructor(
        private val repository: TodoItemsRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return SynchronizeWorker(
                appContext,
                params,
                repository
            )
        }

    }

}