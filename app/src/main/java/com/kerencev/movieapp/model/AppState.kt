package com.kerencev.movieapp.model

import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.entities.MoviesList

sealed class AppState {
    data class Success(val moviesData: List<MoviesList>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
