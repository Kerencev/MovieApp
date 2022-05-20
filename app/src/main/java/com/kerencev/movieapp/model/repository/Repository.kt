package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.model.entities.Movie

interface Repository {

    fun getMoviesFromServer(): List<Movie>
    fun getMoviesFromLocalStorage(): List<Movie>
}