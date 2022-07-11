package com.kerencev.movieapp.model.appstate

import com.kerencev.movieapp.data.database.entities.HistoryEntity

sealed class HistoryState {
    data class Success(val moviesData: List<HistoryEntity>) : HistoryState()
    object Error : HistoryState()
    object Loading : HistoryState()
}
