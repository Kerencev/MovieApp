package com.kerencev.movieapp.model

import com.kerencev.movieapp.model.entities.Movie

sealed class AppState {

    data class Success(val moviesData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()

}
