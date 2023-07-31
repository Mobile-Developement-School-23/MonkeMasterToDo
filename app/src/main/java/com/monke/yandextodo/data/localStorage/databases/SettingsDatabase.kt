package com.monke.yandextodo.data.localStorage.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.monke.yandextodo.data.localStorage.dao.SettingsDao
import com.monke.yandextodo.data.localStorage.roomModels.SettingsRoom

@Database(
    entities = [SettingsRoom::class],
    version = 1
)
abstract class SettingsDatabase: RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
}