package com.kerencev.movieapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerencev.movieapp.data.loaders.entities.list.COLOR_NULL
import com.kerencev.movieapp.data.loaders.entities.list.MovieApi
import com.kerencev.movieapp.data.loaders.entities.name.CastMovy
import com.kerencev.movieapp.data.loaders.entities.name.KnownFor
import com.kerencev.movieapp.data.loaders.entities.name.NameData
import com.kerencev.movieapp.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: Repository) : ViewModel() {
    private val localNameData = MutableLiveData<NameData?>()
    val nameData: LiveData<NameData?> get() = localNameData
    private val localFeatMovieData = MutableLiveData<List<MovieApi>>()
    val featMovieData: LiveData<List<MovieApi>> get() = localFeatMovieData

    fun getData(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getNameDataFromServer(id)
            localNameData.postValue(data)
            data?.let { postFeatMovieData(data.knownFor, data.castMovies) }
        }
    }

    private fun postFeatMovieData(knownForList: List<KnownFor>, castMovieList: List<CastMovy>) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieApiList: ArrayList<MovieApi> = arrayListOf()
            knownForList.forEach {
                movieApiList.add(
                    MovieApi(
                        id = it.id,
                        title = it.title,
                        year = it.year,
                        image = it.image,
                        imDbRating = "",
                        colorOfRating = COLOR_NULL
                    )
                )
            }
            castMovieList.forEach {
                movieApiList.add(
                    MovieApi(
                        id = it.id,
                        title = it.title,
                        year = it.year,
                        imDbRating = "",
                        image = null,
                        colorOfRating = COLOR_NULL
                    )
                )
            }
            localFeatMovieData.postValue(movieApiList)
        }
    }
}