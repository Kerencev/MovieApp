package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.list.MoviesListApi
import com.kerencev.movieapp.model.appstate.MainState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

const val CATEGORY_MOST_POPULAR = "MostPopularMovies"
const val CATEGORY_TOP_250 = "Top250Movies"
const val CATEGORY_COMING_SOON = "ComingSoon"

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<MainState>()
    val liveData: LiveData<MainState> get() = localLiveData

    fun getMovies(categories: ArrayList<String>) = getDataFromServer(categories)
//    fun getMovies() = getDataFromLocalStorage()

    private fun getDataFromServer(categories: ArrayList<String>) {
        val result = ArrayList<MoviesListApi>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                categories.forEach { category ->
                    val dto = repository.getMoviesFromServer(category)
                    dto?.let { result.add(it) }
                }
                localLiveData.postValue(MainState.Success(result))
            } catch (e: IOException) {
                localLiveData.postValue(MainState.Error)
            }
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

//    private fun getDataFromLocalStorage() {
//        localLiveData.value = MainState.Loading
//        val result1 = repository.getMoviesFromLocalStorage()
//        val result = listOf(result1, result1, result1)
//        localLiveData.postValue(MainState.Success(result))
//    }
}