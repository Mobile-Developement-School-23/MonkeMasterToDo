package com.monke.yandextodo.ioc.modules

import com.monke.yandextodo.utils.workers.ChildWorkerFactory
import com.monke.yandextodo.utils.workers.SynchronizeWorker
import dagger.Binds
import dagger.Module


@Module
interface WorkerModule {

    @Binds
    fun bindSynchronizeWorker(factory: SynchronizeWorker.Factory) : ChildWorkerFactory
}