package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveDataIsClearHistory = MutableLiveData<Boolean>()
    val liveDataIsClearHistory: LiveData<Boolean> get() = localLiveDataIsClearHistory

    private val _isClearSearchHistory = MutableLiveData<Boolean>()
    val isClearSearchHistory: LiveData<Boolean> get() = _isClearSearchHistory

    private val _isClearFavorites = MutableLiveData<Boolean>()
    val isClearFavorites: LiveData<Boolean> get() = _isClearFavorites

    fun clearHistoryFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.isHistoryEmpty()) {
                true -> localLiveDataIsClearHistory.postValue(true)
                false -> {
                    localLiveDataIsClearHistory.postValue(false)
                    repository.clearHistory()
                }
            }
        }
    }

    fun clearSearchHistoryFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.isSearchHistoryEmpty()) {
                true -> _isClearSearchHistory.postValue(true)
                false -> {
                    _isClearSearchHistory.postValue(false)
                    repository.clearSearchHistory()
                }
            }
        }
    }

    fun cleanFavoritesFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            when (repository.isFavoritesEmpty()) {
                true -> _isClearFavorites.postValue(true)
                false -> {
                    _isClearFavorites.postValue(false)
                    repository.clearFavorites()
                }
            }
        }
    }
}