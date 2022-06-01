package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kerencev.movieapp.data.entities.MovieApi
import com.kerencev.movieapp.model.AppState
import com.kerencev.movieapp.model.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> get() = localLiveData

    fun getMovies() = getDataFromServer()

    private fun getDataFromServer() {
        localLiveData.value = AppState.Loading
        Thread {
            val mostPopular = repository.getMoviesFromServer("MostPopularMovies")
            val top250 = repository.getMoviesFromServer("Top250Movies")
            val comingSoon = repository.getMoviesFromServer("ComingSoon")

            if (checkNullMovies(mostPopular, top250, comingSoon)) {
                localLiveData.postValue(AppState.Error)
                return@Thread
            }

            val list = listOf(mostPopular, top250, comingSoon)
            localLiveData.postValue(AppState.Success(list))
        }.start()
    }

    /**
     * Если хотя бы один списиок будет равен null, то [AppState.Error]
     */
    private fun checkNullMovies(vararg lists: List<MovieApi>?): Boolean {
        for (list in lists) {
            if (list == null) {
                return true
            }
        }
        return false
    }
}