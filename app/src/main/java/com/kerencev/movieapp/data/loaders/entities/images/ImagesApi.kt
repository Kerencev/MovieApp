package com.kerencev.movieapp.data.loaders.entities.images

data class ImagesApi(
    val errorMessage: String?,
    val fullTitle: String?,
    val imDbId: String?,
    val items: List<ImageApi>?,
    val title: String?,
    val type: String?,
    val year: String?
)