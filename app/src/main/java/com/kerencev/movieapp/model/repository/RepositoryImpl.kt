package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.MovieLoader
import com.kerencev.movieapp.data.entities.MovieApi

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(category: String): List<MovieApi>? {
        val dto = MovieLoader.loadMovies(category)
        return dto?.items
    }

    override fun getMoviesFromLocalStorage(): List<MovieApi>? {
        return null
    }
}