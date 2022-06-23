package com.kerencev.movieapp.data.preferences

import android.app.Activity
import android.content.Context

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