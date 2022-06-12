package com.kerencev.movieapp.data.loaders.entities.details

data class Images(
    val errorMessage: String,
    val fullTitle: String,
    val imDbId: String,
    val items: List<Item>,
    val title: String,
    val type: String,
    val year: String
)