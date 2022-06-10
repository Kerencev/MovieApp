package com.kerencev.movieapp.model.helpers

import java.lang.StringBuilder

object FormatActorName {

    fun getName(name: String): String {
        val arr = name.split(" ")
        val startIndex = arr.size / 2
        val result = StringBuilder()
        for (i in startIndex until arr.size) {
            result.append("${arr[i]} ")
        }
        return result.toString()
    }
}