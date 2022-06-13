package com.kerencev.movieapp.data.database

import androidx.room.RoomDatabase
import com.kerencev.movieapp.data.database.dao.LikedMovieDao
import com.kerencev.movieapp.data.database.dao.NoteDao
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity

@androidx.room.Database(
    version = 2,
    exportSchema = true,
    entities = [
        LikedMovieEntity::class,
        NoteEntity::class
    ]
)
abstract class DataBase : RoomDatabase() {
    abstract fun likedMovieDao(): LikedMovieDao
    abstract fun noteDao(): NoteDao
}