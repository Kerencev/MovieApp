package com.kerencev.movieapp.data.preferences

import android.app.Activity
import android.content.Context

const val IS_SAVE_HISTORY_KEY = "IS_SAVE_HISTORY_KEY"
const val IS_SAVE_SEARCH_HISTORY_KEY = "IS_SAVE_SEARCH_HISTORY_KEY"
const val TOP_250_KEY = "TOP_250_KEY"
const val MOST_POPULAR_KEY = "MOST_POPULAR_KEY"
const val COMING_SOON_KEY = "COMING_SOON_KEY"

object Pref {
    fun saveDataIsChecked(activity: Activity?, key: String, isChecked: Boolean) {
        val editor = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(key, isChecked)
        editor?.apply()
    }

    fun getDataIsChecked(activity: Activity?, key: String): Boolean {
        return activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)?.getBoolean(
            key, true
        ) ?: true
    }
}