package com.monke.yandextodo.data.localStorage.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RevisionRoom (
    @PrimaryKey
    var id: String,
    var revision: Int
)