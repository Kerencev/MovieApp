package com.kerencev.movieapp.data.loaders.entities.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val COLOR_RATING_GRAY = "COLOR_RATING_GRAY"
const val COLOR_RATING_RED = "COLOR_RATING_RED"
const val COLOR_RATING_GREEN = "COLOR_RATING_GREEN"
const val COLOR_NULL = "COLOR_NULL"

/**
 * Класс для записи данных конкретного фильма и отображения в списке
 */
@Parcelize
data class MovieApi(
    val id: String?,
    val title: String?,
    val year: String?,
    val imDbRating: String?,
    val image: String?,
    var colorOfRating: String = COLOR_RATING_GREEN
) : Parcelable
