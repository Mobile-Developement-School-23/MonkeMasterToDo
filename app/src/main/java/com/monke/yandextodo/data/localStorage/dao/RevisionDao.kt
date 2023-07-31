package com.monke.yandextodo.data.localStorage.dao

import androidx.room.*
import com.monke.yandextodo.data.localStorage.roomModels.RevisionRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface RevisionDao {

    @Query("SELECT MAX(revision) from revisionRoom")
    fun getLastRevision(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRevision(vararg todoItems: RevisionRoom)

}