package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi

interface Repository {
    fun getMoviesFromServer(category: String): List<MovieApi>?
    fun getMovieDetailsFromServer(id: String): MovieDetailsApi?
    fun getMoviesFromLocalStorage(): List<MovieApi>
}