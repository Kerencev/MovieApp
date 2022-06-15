package com.kerencev.movieapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerencev.movieapp.data.database.entities.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity ORDER BY created_at DESC")
    fun getAll(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity")
    fun deleteAll()
}