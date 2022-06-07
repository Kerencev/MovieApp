package com.kerencev.movieapp.model.receivers

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    private const val TYPE_WIFI = 1
    private const val TYPE_MOBILE = 2
    private const val TYPE_NOT_CONNECTED = 0

    private fun getConnectivityStatus(context: Context): Int {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
        }
        return TYPE_NOT_CONNECTED
    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn: Int = getConnectivityStatus(context)
        var status: String? = null
        when (conn) {
            TYPE_WIFI -> {
                status = "Wifi enabled"
            }
            TYPE_MOBILE -> {
                status = "Mobile data enabled"
            }
            TYPE_NOT_CONNECTED -> {
                status = "Not connected to Internet"
            }
        }
        return status
    }
}