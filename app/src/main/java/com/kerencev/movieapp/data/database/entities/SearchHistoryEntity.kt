package com.kerencev.movieapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistoryEntity(
    @PrimaryKey (autoGenerate = false)
    val id: String,
    val title: String?,
    val image: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long?
)