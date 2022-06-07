package com.kerencev.movieapp.data.entities.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс для записи данных конкретного фильма и отображения в списке
 */
@Parcelize
data class MovieApi(
    val id: String?,
    val title: String?,
    val year: String?,
    val imDbRating: String?,
    val image: String?
) : Parcelable
