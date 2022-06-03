package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<DetailsState>()
    val liveData: LiveData<DetailsState> get() = localLiveData

    fun getMovieDetails(id: String) = getDataFromServer(id)

    private fun getDataFromServer(id: String) {
        localLiveData.value = DetailsState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val movieDetails = repository.getMovieDetailsFromServer(id)
            when (movieDetails) {
                null -> localLiveData.postValue(DetailsState.Error)
                else -> localLiveData.postValue(DetailsState.Success(movieDetails))
            }
        }
    }
}