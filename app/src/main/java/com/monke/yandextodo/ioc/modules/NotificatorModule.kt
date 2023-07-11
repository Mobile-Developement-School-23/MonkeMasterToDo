package com.monke.yandextodo.ioc.modules

import android.app.Application
import android.content.Context
import com.monke.yandextodo.App
import com.monke.yandextodo.ioc.scopes.AppScope
import com.monke.yandextodo.utils.notifications.NotificationScheduler
import com.monke.yandextodo.utils.notifications.Notificator
import dagger.Module
import dagger.Provides

@Module
class NotificatorModule {

    @AppScope
    @Provides
    fun provideNotificator(application: Application) = Notificator(application.applicationContext)

    @AppScope
    @Provides
    fun provideNotificationScheduler(application: Application) =
        NotificationScheduler(application.applicationContext)
}