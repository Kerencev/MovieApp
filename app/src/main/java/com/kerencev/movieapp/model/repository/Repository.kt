package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.entities.name.NameData

interface Repository {
    fun getMoviesFromServer(category: String): List<MovieApi>?
    fun getMovieDetailsFromServer(id: String): MovieDetailsApi?
    fun getNameDataFromServer(id: String): NameData?
    fun getMoviesFromLocalStorage(): List<MovieApi>
}