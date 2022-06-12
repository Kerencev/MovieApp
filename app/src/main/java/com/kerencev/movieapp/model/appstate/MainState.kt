package com.kerencev.movieapp.model.appstate

import com.kerencev.movieapp.data.loaders.entities.list.MovieApi

sealed class MainState {
    data class Success(val moviesData: List<List<MovieApi>?>) : MainState()
    object Error : MainState()
    object Loading : MainState()
}
