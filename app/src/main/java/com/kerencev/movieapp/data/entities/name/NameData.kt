package com.kerencev.movieapp.data.entities.name

data class NameData(
    val awards: String,
    val birthDate: String,
    val castMovies: List<CastMovy>,
    val deathDate: Any,
    val errorMessage: String,
    val height: String,
    val id: String,
    val image: String,
    val knownFor: List<KnownFor>,
    val name: String,
    val role: String,
    val summary: String
)