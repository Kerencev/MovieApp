package com.kerencev.movieapp.model.helpers

import java.lang.StringBuilder
import java.time.Duration

fun formatBudget(budget: Long): String {
    val string = budget.toString()
    val sb = StringBuilder()
    var counter = 1

    for (i in string.length - 1 downTo 0) {
        if (counter == 3) {
            sb.insert(0, string[i])
            if (i != 0) {
                sb.insert(0, " ")
            }
            counter = 1
        } else {
            sb.insert(0, string[i])
            counter++
        }
    }
    sb.append(" $")
    return sb.toString()
}

fun formatTitleDetails(title: String, year: Int): String {
    return "$title ($year)"
}

fun formatDuration(duration: Int): String {
    return "$duration мин."
}