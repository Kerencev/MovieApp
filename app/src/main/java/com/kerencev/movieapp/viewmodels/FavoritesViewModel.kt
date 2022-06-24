package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.model.appstate.FavoritesState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<FavoritesState>()
    val liveData: LiveData<FavoritesState> get() = localLiveData

    fun getFavoritesMovieFromDataBase() {
        localLiveData.value = FavoritesState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllLikedMovie()
            data?.let { it ->
                if (it.isEmpty()) return@launch
                it.forEach { movieApi ->
                    movieApi.myRating = movieApi.id?.let { repository.getNote(it)?.rating }
                    movieApi.myNote = movieApi.id?.let { repository.getNote(it)?.note }
                }
                localLiveData.postValue(FavoritesState.Success(it))
            }
        }
    }
}