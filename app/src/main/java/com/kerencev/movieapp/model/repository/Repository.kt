package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData

interface Repository {
    fun getMoviesFromServer(category: String): List<MovieApi>?
    fun getMovieDetailsFromServer(id: String): MovieDetailsApi?
    fun getNameDataFromServer(id: String): NameData?
    fun getMoviesFromLocalStorage(): List<MovieApi>
    fun saveEntity(movie: MovieApi)
    fun deleteEntity(id: String)
    fun getAllLikedMovie(): List<MovieApi>
    fun isLikedMovie(id: String): Boolean
}