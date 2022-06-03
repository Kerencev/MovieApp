package com.kerencev.movieapp.model.appstate

import com.kerencev.movieapp.data.entities.details.MovieDetailsApi

sealed class DetailsState {
    data class Success(val moviesData: MovieDetailsApi?) : DetailsState()
    object Error : DetailsState()
    object Loading : DetailsState()
}
