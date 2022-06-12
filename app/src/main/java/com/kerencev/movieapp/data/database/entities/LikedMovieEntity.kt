package com.kerencev.movieapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedMovieEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val poster: String,
    val title: String,
    val rating: String,
    val year: String
) {
}