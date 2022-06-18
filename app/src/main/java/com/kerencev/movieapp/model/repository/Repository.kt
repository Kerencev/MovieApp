package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData

interface Repository {
    fun getMoviesFromServer(category: String): List<MovieApi>?
    fun getMovieDetailsFromServer(id: String): MovieDetailsApi?
    fun getMovieDetailsFromLocalStorage(): MovieDetailsApi
    fun getNameDataFromServer(id: String): NameData?
    fun getMoviesFromLocalStorage(): List<MovieApi>
    fun saveLikedMovieEntity(movie: MovieDetailsApi)
    fun deleteLikedMovieEntity(id: String)
    fun getAllLikedMovie(): List<MovieApi>?
    fun isLikedMovie(id: String): Boolean
    fun saveNoteEntity(note: NoteEntity)
    fun getNote(id: String): NoteEntity?
    fun saveHistory(movie: MovieDetailsApi)
    fun getAllHistory(): List<HistoryEntity>
    fun clearHistory()
}