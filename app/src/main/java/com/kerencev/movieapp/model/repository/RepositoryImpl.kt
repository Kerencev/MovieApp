package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.R
import com.kerencev.movieapp.model.entities.Movie
import com.kerencev.movieapp.model.entities.MoviesList

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(): List<Movie> {
        return ArrayList()
    }

    override fun getMoviesFromLocalStorage(): List<MoviesList> {
        val result: ArrayList<MoviesList> = ArrayList()
        val tempMovies = listOf(
            Movie(title = "Мстители", genre = "Фантастика", rating = 4.4, year = 2012, R.drawable.avengers),
            Movie(title = "Криминальное чтиво", genre = "Боевик", rating = 9.4, year = 2001, R.drawable.criminal),
            Movie(title = "Однажды в голливуде", genre = "Комедия", rating = 8.1, year = 2015, R.drawable.hollywood),
            Movie(title = "Легенда", genre = "Боевик", rating = 6.6, year = 2017, R.drawable.legend),
            Movie(title = "Матрица", genre = "Фантастика", rating = 5.0, year = 2000, R.drawable.matrix),
            Movie(title = "Доктор Стрендж", genre = "Фантастика", rating = 8.8, year = 2014, R.drawable.strange),
        )
        val tempMovies1 = listOf(
            Movie(title = "Легенда", genre = "Боевик", rating = 6.6, year = 2017, R.drawable.legend),
            Movie(title = "Матрица", genre = "Фантастика", rating = 5.0, year = 2000, R.drawable.matrix),
            Movie(title = "Доктор Стрендж", genre = "Фантастика", rating = 8.8, year = 2014, R.drawable.strange),
            Movie(title = "Мстители", genre = "Фантастика", rating = 4.4, year = 2012, R.drawable.avengers),
            Movie(title = "Криминальное чтиво", genre = "Боевик", rating = 9.4, year = 2001, R.drawable.criminal),
            Movie(title = "Однажды в голливуде", genre = "Комедия", rating = 8.1, year = 2015, R.drawable.hollywood)
        )
        val tempMovies2 = listOf(
            Movie(title = "Однажды в голливуде", genre = "Комедия", rating = 8.1, year = 2015, R.drawable.hollywood),
            Movie(title = "Легенда", genre = "Боевик", rating = 6.6, year = 2017, R.drawable.legend),
            Movie(title = "Матрица", genre = "Фантастика", rating = 5.0, year = 2000, R.drawable.matrix),
            Movie(title = "Доктор Стрендж", genre = "Фантастика", rating = 8.8, year = 2014, R.drawable.strange),
            Movie(title = "Мстители", genre = "Фантастика", rating = 4.4, year = 2012, R.drawable.avengers),
            Movie(title = "Криминальное чтиво", genre = "Боевик", rating = 9.4, year = 2001, R.drawable.criminal)
        )
        val tempMoviesList = MoviesList("Топ фильмов", tempMovies)
        val tempMoviesList1 = MoviesList("Новинки", tempMovies1)
        val tempMoviesList2 = MoviesList("Недавно просмотренные", tempMovies2)
        val tempMoviesList3 = MoviesList("Выбор зрителей", tempMovies)
        result.add(tempMoviesList)
        result.add(tempMoviesList1)
        result.add(tempMoviesList2)
        result.add(tempMoviesList3)
        return result
    }
}