package com.kerencev.movieapp.model

import com.kerencev.movieapp.data.entities.MovieApi

sealed class AppState {
    data class Success(val moviesData: List<List<MovieApi>?>) : AppState()
    object Error : AppState()
    object Loading : AppState()
}
