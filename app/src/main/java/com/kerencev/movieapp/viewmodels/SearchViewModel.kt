package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.loaders.entities.search.SearchedMovies
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<SearchedMovies?>()
    val liveData: LiveData<SearchedMovies?> get() = localLiveData

    fun getData(title: String) {
        when {
            title.isEmpty() -> return
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val dto = repository.searchMoviesFromServer(title)
                            localLiveData.postValue(dto)
                        } catch (e: IOException) {
                            localLiveData.postValue(null)
                        }
                }
            }
        }
    }
}