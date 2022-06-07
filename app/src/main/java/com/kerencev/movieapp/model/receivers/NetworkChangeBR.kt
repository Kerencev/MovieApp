package com.kerencev.movieapp.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NetworkChangeBR : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val status = NetworkUtil.getConnectivityStatusString(context!!)
        Toast.makeText(context, status, Toast.LENGTH_LONG).show()
    }
}