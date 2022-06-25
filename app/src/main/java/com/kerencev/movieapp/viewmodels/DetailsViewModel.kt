package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.details.Similar
import com.kerencev.movieapp.data.loaders.entities.images.ImagesApi
import com.kerencev.movieapp.data.loaders.entities.list.*
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.data.loaders.entities.trailer.YouTubeTrailer
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.helpers.FormatActorName
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private var movieData: MovieDetailsApi? = null

    private val localLiveData = MutableLiveData<DetailsState>()
    val liveData: LiveData<DetailsState> get() = localLiveData

    private val localLiveNameData = MutableLiveData<List<NameData>>()
    val liveNameData: LiveData<List<NameData>> get() = localLiveNameData

    private val localLiveDataIsLiked = MutableLiveData<Boolean>(false)
    val liveDataIsLiked: MutableLiveData<Boolean> get() = localLiveDataIsLiked

    private val localNoteData = MutableLiveData<NoteEntity?>()
    val noteData: MutableLiveData<NoteEntity?> get() = localNoteData

    private val _imagesData = MutableLiveData<ImagesApi?>()
    val imagesData: LiveData<ImagesApi?> get() = _imagesData

    private val _trailerData = MutableLiveData<YouTubeTrailer?>()
    val trailerData: LiveData<YouTubeTrailer?> get() = _trailerData

    fun getMovieDetails(id: String, isSaveHistory: Boolean) = getDataFromServer(id, isSaveHistory)
//    fun getMovieDetails(id: String) = getDataFromLocalStorage()

    fun isLikedMovie(id: String) = checkLikedMovieDataBase(id)

    fun saveLikedMovieInDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            val isLikedMovie = repository.isLikedMovie(movieData?.id ?: "0")
            if (isLikedMovie) {
                movieData?.let { it.id?.let { it1 -> repository.deleteLikedMovieEntity(it1) } }
                localLiveDataIsLiked.postValue(false)
            } else {
                movieData?.let { repository.saveLikedMovieEntity(it) }
                localLiveDataIsLiked.postValue(true)
            }
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getNote(id)
            localNoteData.postValue(data)
        }
    }

    fun getImagesFromServer(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getImages(id)
                _imagesData.postValue(data)
            } catch (e: IOException) {
                _imagesData.postValue(null)
            }
        }
    }

    fun getTrailerFromServer(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getTrailerDataFromServer(id)
                _trailerData.postValue(data)
            } catch (e: IOException) {
                _trailerData.postValue(null)
            }
        }
    }

    fun convertListSimillarsToListMovieApi(data: List<Similar?>): List<MovieApi> {
        val result = mutableListOf<MovieApi>()
        data.forEach { similar ->
            similar?.let {
                result.add(
                    MovieApi(
                        id = similar.id,
                        title = similar.title,
                        year = null,
                        imDbRating = similar.imDbRating,
                        image = similar.image,
                        colorOfRating = setRightColor(similar.imDbRating)
                    )
                )
            }
        }
        return result
    }

    private fun getDataFromLocalStorage() {
        val movieDetails = repository.getMovieDetailsFromLocalStorage()
        localLiveData.postValue(DetailsState.Success(movieDetails))
        movieData = movieDetails
        saveHistory(movieDetails)
    }

    private fun saveHistory(movie: MovieDetailsApi) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveHistory(movie)
        }
    }

    private fun checkLikedMovieDataBase(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localLiveDataIsLiked.postValue(repository.isLikedMovie(id))
        }
    }

    private fun getDataFromServer(id: String, isSaveHistory: Boolean) {
        localLiveData.value = DetailsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val movieDetails = repository.getMovieDetailsFromServer(id)) {
                null -> localLiveData.postValue(DetailsState.Error)
                else -> {
                    formatActorsListName(movieDetails)
                    localLiveData.postValue(DetailsState.Success(movieDetails))
                    getNameDataFromServer(getIdDirectorsList(movieDetails))
                    movieData = movieDetails
                    if (isSaveHistory) {
                        saveHistory(movieDetails)
                    }
                }
            }
        }
    }

    private fun getNameDataFromServer(idList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mutableListOf<NameData>()
            idList.forEach() { id ->
                val nameData = repository.getNameDataFromServer(id)
                nameData?.let { result.add(it) }
            }
            localLiveNameData.postValue(result)
        }
    }

    private fun formatActorsListName(movieDetails: MovieDetailsApi) {
        movieDetails.actorList?.forEach() { actor ->
            actor.asCharacter = actor.asCharacter?.let { FormatActorName.getName(it) }
        }
    }

    private fun getIdDirectorsList(moviesData: MovieDetailsApi): List<String> {
        val idList = mutableListOf<String>()
        moviesData.directorList?.forEach() { director ->
            director.id?.let { idList.add(it) }
        }
        return idList
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