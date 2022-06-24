package com.kerencev.movieapp.data.loaders.entities.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Класс для записи ответа с сервера по запросу списка фильмов
 */
@Parcelize
data class MoviesListApi(
    var title: String,
    val items: List<MovieApi>?,
    val errorMessage: String?
): Parcelable
