package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.database.entities.NoteEntity
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import com.kerencev.movieapp.model.appstate.DetailsState
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: Repository) : ViewModel() {
    private var rating = 0
    private val localNoteData = MutableLiveData<NoteEntity?>()
    val noteData: MutableLiveData<NoteEntity?> get() = localNoteData

    fun setRating(rating: Int) {
        this.rating = rating
    }

    fun saveNoteInDataBase(id: String, note: String) {
        if (rating == 0 && note.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveNoteEntity(
                NoteEntity(
                    id = id,
                    rating = rating,
                    note = note
                )
            )
        }
    }

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getNote(id)
            localNoteData.postValue(data)
        }
    }
}