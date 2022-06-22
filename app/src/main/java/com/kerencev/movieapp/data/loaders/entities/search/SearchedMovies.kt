package com.kerencev.movieapp.data.loaders.entities.search

data class SearchedMovies(
    val errorMessage: String?,
    val expression: String?,
    val results: List<Result>?,
    val searchType: String?
)