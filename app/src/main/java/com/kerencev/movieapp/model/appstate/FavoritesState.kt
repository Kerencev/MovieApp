package com.kerencev.movieapp.model.appstate

import com.kerencev.movieapp.data.loaders.entities.list.MovieApi

sealed class FavoritesState {
    data class Success(val moviesData: List<MovieApi>) : FavoritesState()
    object Error : FavoritesState()
    object Loading : FavoritesState()
}