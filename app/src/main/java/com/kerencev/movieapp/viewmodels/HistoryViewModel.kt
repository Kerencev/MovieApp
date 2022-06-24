package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.model.appstate.HistoryState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {
    private val localLiveData = MutableLiveData<HistoryState>()
    val liveData: LiveData<HistoryState> get() = localLiveData

    private val localLiveDataIsClearHistory = MutableLiveData<Boolean>()
    val liveDataIsClearHistory: LiveData<Boolean> get() = localLiveDataIsClearHistory

    private val _isClearSearchHistory = MutableLiveData<Boolean>()
    val isClearSearchHistory: LiveData<Boolean> get() = _isClearSearchHistory

    fun getHistoryFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllHistory()
            localLiveData.postValue(HistoryState.Success(data))
        }
    }

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
}