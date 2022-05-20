package com.kerencev.movieapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kerencev.movieapp.model.AppState
import com.kerencev.movieapp.model.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> get() = localLiveData

    fun getMovies() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        localLiveData.value = AppState.Loading
        Thread {
            Thread.sleep(2000L)
            localLiveData.postValue(AppState.Success(repository.getMoviesFromLocalStorage()))
        }.start()
    }
}