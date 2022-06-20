package com.kerencev.movieapp.model.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object FormatBirthDate {
    @SuppressLint("SimpleDateFormat")
    fun init(date: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate =  sdf.format(Date())
        val arrBirtDateString = date.split("-")
        val arrCurrentDateString = currentDate.split("-")
        val arrBirthDate: ArrayList<Int> = arrayListOf()
        val arrCurrentDate: ArrayList<Int> = arrayListOf()
        arrBirtDateString.forEach {
            arrBirthDate.add(it.toInt())
        }
        arrCurrentDateString.forEach {
            arrCurrentDate.add(it.toInt())
        }
        var howOld = arrCurrentDate[0] - arrBirthDate[0]
        if (arrBirthDate[1] < arrCurrentDate[1]) {
            howOld++
        }
        if (arrBirthDate[1] == arrCurrentDate[1] && arrBirthDate[2] < arrCurrentDate[2]) {
            howOld++
        }
        return "$date \n $howOld y.o"
    }
}