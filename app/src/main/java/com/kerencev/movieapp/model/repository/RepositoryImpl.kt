package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.database.DataBase
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.data.loaders.MovieLoaderRetrofit

class RepositoryImpl(private val db: DataBase) : Repository {
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

    override fun getNameDataFromServer(id: String): NameData? {
        return when (val dto = MovieLoaderRetrofit.create().getNameData(id).execute().body()) {
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

    override fun saveLikedMovieEntity(movie: MovieApi) {
        db.likedMovieDao().insert(convertMovieApiToLikedMovieEntity(movie))
    }

    override fun deleteLikedMovieEntity(id: String) {
        db.likedMovieDao().deleteById(id)
    }

    override fun getAllLikedMovie(): List<MovieApi> {
        return convertLikedMovieEntityToMovieApi(db.likedMovieDao().getAll())
    }

    override fun isLikedMovie(id: String): Boolean {
        return db.likedMovieDao().exists(id)
    }

    override fun saveNoteEntity(note: NoteEntity) {
        db.noteDao().insert(note)
    }

    override fun getNote(id: String): NoteEntity {
        return db.noteDao().getById(id)
    }

    private fun convertLikedMovieEntityToMovieApi(entityList: List<LikedMovieEntity>): List<MovieApi> {
        return entityList.map { likedMovieEntity ->
            MovieApi(
                id = likedMovieEntity.id,
                title = likedMovieEntity.title,
                year = likedMovieEntity.year,
                imDbRating = likedMovieEntity.rating,
                image = likedMovieEntity.poster
                )
        }
    }

    private fun convertMovieApiToLikedMovieEntity(movie: MovieApi): LikedMovieEntity {
        return LikedMovieEntity(
            id = movie.id!!,
            poster = movie.image!!,
            title = movie.title!!,
            rating = movie.imDbRating ?: "0.0",
            year = movie.year!!
        )
    }
}
