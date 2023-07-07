package com.monke.yandextodo.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class SynchronizeWorkerFactory @Inject constructor(
    private val factoryProvider: Provider<ChildWorkerFactory>
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return factoryProvider.get().create(appContext, workerParameters)
    }


}