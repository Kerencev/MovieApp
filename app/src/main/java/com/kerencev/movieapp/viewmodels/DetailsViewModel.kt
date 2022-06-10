package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.entities.name.NameData
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.helpers.FormatActorName
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<DetailsState>()
    val liveData: LiveData<DetailsState> get() = localLiveData
    private val localLiveNameData = MutableLiveData<List<NameData>>()
    val liveNameData: LiveData<List<NameData>> get() = localLiveNameData

    fun getMovieDetails(id: String) = getDataFromServer(id)

    private fun getDataFromServer(id: String) {
        localLiveData.value = DetailsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val movieDetails = repository.getMovieDetailsFromServer(id)) {
                null -> localLiveData.postValue(DetailsState.Error)
                else -> {
                    formatActorsListName(movieDetails)
                    localLiveData.postValue(DetailsState.Success(movieDetails))
                    getNameDataFromServer(getIdDirectorsList(movieDetails))
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
}