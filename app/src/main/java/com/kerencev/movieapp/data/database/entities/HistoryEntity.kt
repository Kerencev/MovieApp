package com.kerencev.movieapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val poster: String,
    val title: String,
    val rating: String,
    val year: String,
    val date: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
