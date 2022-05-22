package com.kerencev.movieapp.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val title: String = "Во все тяжкие",
    val genre: String = "Комедия",
    val rating: Double = 8.0,
    val year: Int = 2020,
    val poster: Int,
    val duration: Int = 200,
    val budget: Long = 87878545,
    val description: String = "Описание"
) : Parcelable