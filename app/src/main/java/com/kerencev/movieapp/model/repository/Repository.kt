package com.kerencev.movieapp.model.repository

import com.kerencev.movieapp.data.database.entities.HistoryEntity
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.details.Images
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.images.ImagesApi
import com.kerencev.movieapp.data.loaders.entities.list.MoviesListApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.data.loaders.entities.search.SearchedMovies
import com.kerencev.movieapp.data.loaders.entities.trailer.YouTubeTrailer

interface Repository {
    fun getMoviesFromServer(category: String): MoviesListApi?
    fun searchMoviesFromServer(title: String): SearchedMovies?
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
    fun isHistoryEmpty(): Boolean
    fun clearFavorites()
    fun isFavoritesEmpty(): Boolean
    fun saveSearchHistory(data: SearchedMovies?)
    fun getSearchHistory(): SearchedMovies
    fun clearSearchHistory()
    fun isSearchHistoryEmpty(): Boolean
    fun getImages(id: String): ImagesApi?
    fun getTrailerDataFromServer(id: String): YouTubeTrailer?
}