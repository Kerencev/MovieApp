package com.kerencev.movieapp.model.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object MyDate {
    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val formatter = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.UK)
        formatter.timeZone = TimeZone.getDefault()
        val date = Date()
        return formatter.format(date)
    }
}