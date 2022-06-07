package com.kerencev.movieapp.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.kerencev.movieapp.services.GetMovieIdService

class LoadMovieDetailsBR : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getStringExtra(GetMovieIdService.INTENT_EXTRA_STRING_ID)
        Toast.makeText(context, "Id: $id", Toast.LENGTH_SHORT).show()
        context?.let { GetMovieIdService.stop(it) }
    }
}