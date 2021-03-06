package com.kerencev.movieapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("DELETE FROM LikedMovieEntity")
    fun deleteAll()

    @Query("SELECT * FROM LikedMovieEntity ORDER BY id LIMIT 1")
    fun getFirst(): LikedMovieEntity?
}