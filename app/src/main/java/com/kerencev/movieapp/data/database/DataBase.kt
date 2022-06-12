package com.kerencev.movieapp.data.database

import androidx.room.RoomDatabase
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity

@androidx.room.Database(
    version = 1,
    exportSchema = false,
    entities = [
        LikedMovieEntity::class
    ]
)
abstract class DataBase : RoomDatabase() {
    abstract fun likedMovieDao(): LikedMovieDao
}