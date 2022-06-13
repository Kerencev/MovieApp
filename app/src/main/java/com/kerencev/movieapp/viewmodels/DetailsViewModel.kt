package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.helpers.FormatActorName
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private var movieData: MovieDetailsApi? = null
    private val localLiveData = MutableLiveData<DetailsState>()
    val liveData: LiveData<DetailsState> get() = localLiveData
    private val localLiveNameData = MutableLiveData<List<NameData>>()
    val liveNameData: LiveData<List<NameData>> get() = localLiveNameData
    private val localLiveDataIsLiked = MutableLiveData<Boolean>(false)
    val liveDataIsLiked: MutableLiveData<Boolean> get() = localLiveDataIsLiked

    fun getMovieDetails(id: String) = getDataFromServer(id)
    fun isLikedMovie(id: String) = checkLikedMovieDataBase(id)

    private fun checkLikedMovieDataBase(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localLiveDataIsLiked.postValue(repository.isLikedMovie(id))
        }
    }

    private fun getDataFromServer(id: String) {
        localLiveData.value = DetailsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val movieDetails = repository.getMovieDetailsFromServer(id)) {
                null -> localLiveData.postValue(DetailsState.Error)
                else -> {
                    formatActorsListName(movieDetails)
                    localLiveData.postValue(DetailsState.Success(movieDetails))
                    getNameDataFromServer(getIdDirectorsList(movieDetails))
                    movieData = movieDetails
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
        movieDetails.actorList.forEach() { actor ->
            actor.asCharacter = FormatActorName.getName(actor.asCharacter)
        }
    }

    private fun getIdDirectorsList(moviesData: MovieDetailsApi): List<String> {
        val idList = mutableListOf<String>()
        moviesData.directorList.forEach() { director ->
            idList.add(director.id)
        }
        return idList
    }

    fun saveLikedMovieInDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            val isLikedMovie = repository.isLikedMovie(movieData?.id ?: "0")
            if (isLikedMovie) {
                movieData?.let { repository.deleteLikedMovieEntity(it.id) }
                localLiveDataIsLiked.postValue(false)
            } else {
                repository.saveLikedMovieEntity(
                    MovieApi(
                        id = movieData?.id,
                        title = movieData?.title,
                        year = movieData?.year,
                        image = movieData?.image,
                        imDbRating = movieData?.imDbRating
                    )
                )
                localLiveDataIsLiked.postValue(true)
            }
        }
    }
}