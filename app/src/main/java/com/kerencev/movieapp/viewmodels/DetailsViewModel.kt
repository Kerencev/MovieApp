package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.model.AppState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> get() = localLiveData

    fun getMovieDetails(id: String) = getDataFromServer(id)

    private fun getDataFromServer(id: String) {
        localLiveData.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val movieDetails = repository.getMovieDetailsFromServer(id)
            localLiveData.postValue(AppState.SuccessLoadMovieDetailsApi(movieDetails))
        }
    }
}