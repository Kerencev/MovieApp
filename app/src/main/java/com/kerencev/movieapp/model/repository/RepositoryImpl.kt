package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.MovieDetailsLoader
import com.kerencev.movieapp.data.loaders.MovieLoader

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(category: String): List<MovieApi>? {
        val dto = MovieLoader.loadMovies(category)
        return dto?.items
    }

    override fun getMovieDetailsFromServer(id: String): MovieDetailsApi? {
        return MovieDetailsLoader.loadDetails(id)
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
        for (i in 1..20) {
            result.addAll(list)
        }
        return result
    }
}
