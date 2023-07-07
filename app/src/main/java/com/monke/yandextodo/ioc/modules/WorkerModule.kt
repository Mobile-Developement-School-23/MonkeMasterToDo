package com.monke.yandextodo.ioc.modules

import com.monke.yandextodo.workers.ChildWorkerFactory
import com.monke.yandextodo.workers.SynchronizeWorker
import dagger.Binds
import dagger.Module


@Module
interface WorkerModule {

    @Binds
    fun bindSynchronizeWorker(factory: SynchronizeWorker.Factory) : ChildWorkerFactory
}