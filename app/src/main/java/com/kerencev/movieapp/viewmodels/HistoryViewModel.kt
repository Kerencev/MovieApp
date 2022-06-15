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

    fun getHistoryFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getAllHistory()
            localLiveData.postValue(HistoryState.Success(data))
        }
    }

    fun clearHistoryFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistory()
        }
    }
}