package com.monke.yandextodo.data.repository

import com.monke.yandextodo.data.localStorage.databases.SettingsDatabase
import com.monke.yandextodo.data.localStorage.roomModels.SettingsRoom
import com.monke.yandextodo.domain.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsDatabase: SettingsDatabase
) {

    suspend fun getSettings() = withContext(Dispatchers.IO) {
        return@withContext settingsDatabase.settingsDao().getSettings().map { it?.toModel() }
    }

    suspend fun setSettings(appSettings: AppSettings) = withContext(Dispatchers.IO) {
        settingsDatabase.settingsDao().setSettings(appSettings.toRoom())
    }

    private fun AppSettings.toRoom() = SettingsRoom(
        themeMode = this.themeMode
    )

    private fun SettingsRoom.toModel() = AppSettings(
        themeMode = this.themeMode
    )

}
