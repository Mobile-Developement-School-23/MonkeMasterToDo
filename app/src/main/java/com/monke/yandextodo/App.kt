package com.monke.yandextodo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.*
import com.monke.yandextodo.data.repository.SettingsRepository
import com.monke.yandextodo.ioc.components.DaggerApplicationComponent
import com.monke.yandextodo.utils.workers.TodoWorkerFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


class App: Application() {

    var applicationComponent = DaggerApplicationComponent.builder().application(this).build()
        private set

    @Inject
    lateinit var todoWorkerFactory: TodoWorkerFactory

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {

        applicationComponent.inject(this)

        // Инициализация Work Manager
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(todoWorkerFactory)
                .build())

        MainScope().launch {
            loadSettings()
        }

        super.onCreate()
    }

    private suspend fun loadSettings() {
        val settings = settingsRepository.getSettings().first()

        if (settings != null) {
            AppCompatDelegate.setDefaultNightMode(settings.themeMode)
        } else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

}