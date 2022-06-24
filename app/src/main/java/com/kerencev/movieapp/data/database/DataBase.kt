package com.kerencev.movieapp.data.database

import androidx.room.RoomDatabase
import com.kerencev.movieapp.data.database.dao.HistoryDao
import com.kerencev.movieapp.data.database.dao.LikedMovieDao
import com.kerencev.movieapp.data.database.dao.NoteDao
import com.kerencev.movieapp.data.database.dao.SearchHistoryDao
import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.database.entities.SearchHistoryEntity

@androidx.room.Database(
    version = 10,
    exportSchema = true,
    entities = [
        LikedMovieEntity::class,
        NoteEntity::class,
        HistoryEntity::class,
        SearchHistoryEntity::class
    ]
)
abstract class DataBase : RoomDatabase() {
    abstract fun likedMovieDao(): LikedMovieDao
    abstract fun noteDao(): NoteDao
    abstract fun historyDao(): HistoryDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}