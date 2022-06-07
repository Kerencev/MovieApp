package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.MovieLoaderRetrofit

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(category: String): List<MovieApi>? {
        return when (val dto = MovieLoaderRetrofit.create().getMovies(category).execute().body()) {
            null -> null
            else -> dto.items
        }
    }

    override fun getMovieDetailsFromServer(id: String): MovieDetailsApi? {
        return when (val dto = MovieLoaderRetrofit.create().getMovieDetails(id).execute().body()) {
            null -> null
            else -> dto
        }
    }


    override fun getMoviesFromLocalStorage(): List<MovieApi> {
        val list = listOf(
            MovieApi(
                id = null,
                title = "Red dead redemption",
                year = "2020",
                imDbRating = "8.0",
                image = "https://imdb-api.com/images/original/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6716_AL_.jpg"
            ), MovieApi(
                id = null,
                title = "Matrix",
                year = "2020",
                imDbRating = "6.4",
                image = "https://imdb-api.com/images/original/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_Ratio0.6716_AL_.jpg"
            )
        )
        val result = mutableListOf<MovieApi>()
        repeat(20) { result.addAll(list) }
        return result
    }
}
