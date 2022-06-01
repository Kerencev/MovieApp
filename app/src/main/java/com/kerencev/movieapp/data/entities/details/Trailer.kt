package com.kerencev.movieapp.data.entities.details

data class Trailer(
    val errorMessage: String,
    val fullTitle: String,
    val imDbId: String,
    val link: String,
    val linkEmbed: String,
    val thumbnailUrl: String,
    val title: String,
    val type: String,
    val uploadDate: Any,
    val videoDescription: String,
    val videoId: String,
    val videoTitle: String,
    val year: String
)