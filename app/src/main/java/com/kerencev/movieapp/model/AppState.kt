package com.kerencev.movieapp.model

import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi

sealed class AppState {
    data class SuccessLoadMovieApiList(val moviesData: List<List<MovieApi>?>) : AppState()
    data class SuccessLoadMovieDetailsApi(val moviesData: MovieDetailsApi?) : AppState()
    object Error : AppState()
    object Loading : AppState()
}
