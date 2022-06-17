package com.kerencev.movieapp.data.loaders.entities.details

data class Posters(
    val backdrops: List<Backdrop>?,
    val errorMessage: String?,
    val fullTitle: String?,
    val imDbId: String?,
    val posters: List<Poster>?,
    val title: String?,
    val type: String?,
    val year: String?
)