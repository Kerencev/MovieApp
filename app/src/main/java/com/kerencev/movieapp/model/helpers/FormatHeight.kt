package com.kerencev.movieapp.model.helpers

object FormatHeight {
    fun init(height: String): String {
        val data = StringBuilder(height)
        val result = StringBuilder()
        for (i in data.length - 2 downTo data.length - 7) {
            result.insert(0, data[i])
        }
        return result.toString()
    }
}