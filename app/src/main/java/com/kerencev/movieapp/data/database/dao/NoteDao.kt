package com.kerencev.movieapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerencev.movieapp.data.database.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    fun getById(id: String): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE id = :id")
    fun deleteById(id: String)

    @Query("SELECT EXISTS (SELECT 1 FROM NoteEntity WHERE id = :id)")
    fun exists(id: String): Boolean
}