package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.entities.list.MovieApi
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<MainState>()
    val liveData: LiveData<MainState> get() = localLiveData

    fun getMovies() = getDataFromServer()
//    fun getMovies() = getDataFromLocalStorage()

    private fun getDataFromServer() {
        localLiveData.value = MainState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val mostPopular = repository.getMoviesFromServer("MostPopularMovies")
//            val top250 = repository.getMoviesFromServer("Top250Movies")
//            val comingSoon = repository.getMoviesFromServer("ComingSoon")

//            if (checkNullMovies(mostPopular, top250, comingSoon)) {
//                localLiveData.postValue(MainState.Error)
//                return@launch
//            }

//            val list = listOf(mostPopular, top250, comingSoon)
            val list = listOf(mostPopular)
            localLiveData.postValue(MainState.Success(list))
        }
    }

    /**
     * Если хотя бы один списиок будет равен null, то [MainState.Error]
     */
    private fun checkNullMovies(vararg lists: List<MovieApi>?): Boolean {
        for (list in lists) {
            if (list == null) {
                return true
            }
        }
        return false
    }

    private fun getDataFromLocalStorage() {
        localLiveData.value = MainState.Loading
        val result1 = repository.getMoviesFromLocalStorage()
        val result = listOf(result1, result1, result1)
        localLiveData.postValue(MainState.Success(result))
    }
}