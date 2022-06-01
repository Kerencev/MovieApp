package com.kerencev.movieapp.data.entities

/**
 * Класс для записи ответа с сервера по запросу списка фильмов
 */
data class MoviesListApi(
    val items: List<MovieApi>?,
    val errorMessage: String?
)
