package com.monke.yandextodo.data.localStorage.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsRoom(
    @PrimaryKey
    val id: Int = 1,
    val themeMode : Int
)