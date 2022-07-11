package com.kerencev.movieapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.kerencev.movieapp.data.loaders.MovieDetailsLoader
import com.kerencev.movieapp.data.loaders.entities.details.MovieDetailsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GetMovieIdService : Service(), CoroutineScope by MainScope() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        launch(Dispatchers.IO) {
            val movieDetails: MovieDetailsApi? =
                intent?.getStringExtra(GetMovieIdService.INTENT_EXTRA_STRING_ID)
                    ?.let { MovieDetailsLoader.loadDetails(it) }

            val intentForBR = Intent()
            intentForBR.action = "com.kerencev.movieapp.load.movie.details"
            intentForBR.putExtra(INTENT_EXTRA_STRING_ID, movieDetails?.id)
            sendBroadcast(intentForBR)
        }
        return START_NOT_STICKY
    }

    companion object {
        const val INTENT_EXTRA_STRING_ID = "INTENT_EXTRA_STRING_ID"

        fun start(context: Context, id: String) {
            val intent = Intent(context, GetMovieIdService::class.java)
            intent.putExtra(INTENT_EXTRA_STRING_ID, id)
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, GetMovieIdService::class.java)
            context.stopService(intent)
        }
    }
}