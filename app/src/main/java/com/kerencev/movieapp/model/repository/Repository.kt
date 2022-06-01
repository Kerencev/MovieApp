package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.entities.MovieApi

interface Repository {
    fun getMoviesFromServer(category: String): List<MovieApi>?
    fun getMoviesFromLocalStorage(): List<MovieApi>?
}