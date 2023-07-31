package com.monke.yandextodo.data.localStorage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monke.yandextodo.data.localStorage.roomModels.SettingsRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * FROM SettingsRoom")
    fun getSettings(): Flow<SettingsRoom?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSettings(settings: SettingsRoom)




}