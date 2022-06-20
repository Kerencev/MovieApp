package com.kerencev.movieapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kerencev.movieapp.data.database.DataBase
import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.data.database.entities.LikedMovieEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.MovieLoaderRetrofit
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.list.*
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.model.helpers.MyDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryImpl(private val db: DataBase) : Repository {
    private val liveData = MutableLiveData<List<MovieApi>?>()
    override fun getMoviesFromServer(category: String): List<MovieApi>? {
        val dto = MovieLoaderRetrofit.create().getMovies(category).execute().body()
        dto?.let {
            dto.items?.forEach { movie ->
                movie.colorOfRating = setRightColor(movie.imDbRating)
            }
            return dto.items
        }
        return null
    }

    override fun getMovieDetailsFromServer(id: String): MovieDetailsApi? {
        return when (val dto = MovieLoaderRetrofit.create().getMovieDetails(id).execute().body()) {
            null -> null
            else -> dto
        }
    }

    override fun getMovieDetailsFromLocalStorage(): MovieDetailsApi {
        return MovieDetailsApi(
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, "8.0", null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, "Red Dead Redemption", null,
            null, null, null, null, null,
            null, "2015"
        )
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

    override fun saveLikedMovieEntity(movie: MovieDetailsApi) {
        db.likedMovieDao().insert(convertMovieDetailsApiToLikedMovieEntity(movie))
    }

    override fun deleteLikedMovieEntity(id: String) {
        db.likedMovieDao().deleteById(id)
    }

    override fun getAllLikedMovie(): List<MovieApi>? {
        return convertLikedMovieEntityToMovieApi(db.likedMovieDao().getAll())
    }

    override fun isLikedMovie(id: String): Boolean {
        return db.likedMovieDao().exists(id)
    }

    override fun saveNoteEntity(note: NoteEntity) {
        db.noteDao().insert(note)
    }

    override fun getNote(id: String): NoteEntity? {
        return db.noteDao().getById(id)
    }

    override fun saveHistory(movie: MovieDetailsApi) {
        db.historyDao().insert(convertMovieDetailsApiToHistoryEntity(movie))
    }

    override fun getAllHistory(): List<HistoryEntity> {
        return db.historyDao().getAll()
    }

    override fun clearHistory() {
        db.historyDao().deleteAll()
    }

    private fun convertLikedMovieEntityToMovieApi(entityList: List<LikedMovieEntity>): List<MovieApi> {
        return entityList.map { likedMovieEntity ->
            MovieApi(
                id = likedMovieEntity.id,
                title = likedMovieEntity.title,
                year = likedMovieEntity.year,
                imDbRating = likedMovieEntity.rating,
                image = likedMovieEntity.poster,
                colorOfRating = likedMovieEntity.colorOfRating
            )
        }
    }

    private fun convertMovieApiToLikedMovieEntity(movie: MovieApi): LikedMovieEntity {
        return LikedMovieEntity(
            id = movie.id!!,
            poster = movie.image!!,
            title = movie.title!!,
            rating = movie.imDbRating ?: "0.0",
            year = movie.year!!,
            colorOfRating = movie.colorOfRating
        )
    }

    //TODO Убрать лишнее в нижних конвертерах(добавил условия для проверки с заглушками)
    private fun convertMovieDetailsApiToHistoryEntity(movie: MovieDetailsApi): HistoryEntity {
        if (movie.id != null) {
            return HistoryEntity(
                id = movie.id,
                poster = movie.image!!,
                title = movie.title!!,
                rating = movie.imDbRating ?: "",
                year = movie.year!!,
                date = MyDate.getDate(),
                createdAt = System.currentTimeMillis(),
                colorOfRating = setRightColor(movie.imDbRating)
            )
        } else {
            return HistoryEntity(
                id = "1",
                poster = "movie.image",
                title = movie.title!!,
                rating = movie.imDbRating!!,
                year = movie.year!!,
                date = MyDate.getDate(),
                createdAt = System.currentTimeMillis(),
                colorOfRating = setRightColor(movie.imDbRating)
            )
        }
    }

    private fun convertMovieDetailsApiToLikedMovieEntity(movie: MovieDetailsApi): LikedMovieEntity {
        if (movie.id != null) {
            return LikedMovieEntity(
                id = movie.id,
                poster = movie.image!!,
                title = movie.title!!,
                rating = movie.imDbRating ?: "",
                year = movie.year!!,
                colorOfRating = setRightColor(movie.imDbRating)
            )
        } else {
            return LikedMovieEntity(
                id = "1",
                poster = "movie.image!!",
                title = movie.title!!,
                rating = movie.imDbRating!!,
                year = movie.year!!,
                colorOfRating = setRightColor(movie.imDbRating)
            )
        }
    }

    private fun setRightColor(rating: String?): String {
        if (rating?.isEmpty() == true) {
            return COLOR_NULL
        }
        val ratingDouble = rating?.toDouble()
        if (ratingDouble == null || ratingDouble == 0.0) {
            return COLOR_NULL
        }
        when {
            ratingDouble < 5.0 -> return COLOR_RATING_RED
            ratingDouble < 7.0 -> return COLOR_RATING_GRAY
        }
        return COLOR_RATING_GREEN
    }
}
