package com.kerencev.movieapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerencev.movieapp.data.database.entities.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM SearchHistoryEntity ORDER BY created_at DESC")
    fun getAll(): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SearchHistoryEntity)

    @Query("DELETE FROM SearchHistoryEntity")
    fun deleteAll()

    @Query("SELECT * FROM SearchHistoryEntity ORDER BY created_at LIMIT 1")
    fun getFirst(): SearchHistoryEntity?
}