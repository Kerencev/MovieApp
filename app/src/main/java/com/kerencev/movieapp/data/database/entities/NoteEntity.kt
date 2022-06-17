package com.kerencev.movieapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val rating: Int,
    val note: String
)