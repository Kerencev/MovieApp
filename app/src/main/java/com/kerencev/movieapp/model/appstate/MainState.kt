package com.kerencev.movieapp.model.appstate

import com.kerencev.movieapp.data.loaders.entities.list.MoviesListApi

sealed class MainState {
    data class Success(val moviesData:  ArrayList<MoviesListApi>) : MainState()
    object Error : MainState()
    object Loading : MainState()
}
