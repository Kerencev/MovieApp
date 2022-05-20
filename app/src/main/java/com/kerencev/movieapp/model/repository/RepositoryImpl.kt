package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.model.entities.Movie

class RepositoryImpl : Repository {
    override fun getMoviesFromServer(): List<Movie> {
        return listOf(Movie(), Movie(title = "Гонка"), Movie(title = "Игрок"), Movie(title = "Карты, деньги, два ствола"))
    }

    override fun getMoviesFromLocalStorage(): List<Movie> {
        val temp: ArrayList<Movie> = ArrayList()
        for (i in 1..1_000) {
            temp.add(Movie("Имя $i", rating = i.toDouble(), year = i))
        }
        return temp
    }
}