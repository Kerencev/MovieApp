package com.kerencev.movieapp.model.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object MyDate {
    fun getDate(): String {
        @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("yyyy.MM.dd")
        val date = Date()
        return formatter.format(date)
    }
}