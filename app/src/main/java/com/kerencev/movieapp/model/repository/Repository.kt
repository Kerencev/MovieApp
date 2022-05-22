package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.entities.MoviesList

interface Repository {

    fun getMoviesFromServer(): List<Movie>
    fun getMoviesFromLocalStorage(): List<MoviesList>
}