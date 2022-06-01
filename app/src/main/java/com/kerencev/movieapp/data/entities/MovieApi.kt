package com.kerencev.movieapp.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс для записи данных конкретного фильма
 */
@Parcelize
data class MovieApi(
    val id: String?,
    val title: String?,
    val year: String?,
    val imDbRating: String?,
    val image: String?
) : Parcelable
