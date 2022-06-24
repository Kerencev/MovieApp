package com.kerencev.movieapp.data.database.dao

import androidx.room.*
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity

@Dao
interface LikedMovieDao {
    @Query("SELECT * FROM LikedMovieEntity")
    fun getAll(): List<LikedMovieEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: LikedMovieEntity)

    @Query("DELETE FROM LikedMovieEntity WHERE id = :id")
    fun deleteById(id: String)

    @Query("SELECT EXISTS (SELECT 1 FROM LikedMovieEntity WHERE id = :id)")
    fun exists(id: String): Boolean
}