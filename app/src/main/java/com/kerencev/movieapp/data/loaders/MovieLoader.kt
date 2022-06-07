package com.kerencev.movieapp.data.loaders

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.kerencev.movieapp.data.entities.list.MoviesListApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val API_KEY = "k_62zj7tzu"

/**
 * Класс для загрузки списка фильмов по категориям через API
 */

object MovieLoader {
    fun loadMovies(category: String): MoviesListApi? {
        val uri = URL("https://imdb-api.com/en/API/$category/$API_KEY")
        lateinit var urlConnection: HttpsURLConnection
        return try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            val bufferReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val lines = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                getLinesForOld(bufferReader)
            } else {
                getLines(bufferReader)
            }

            Gson().fromJson(lines, MoviesListApi::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun getLinesForOld(reader: BufferedReader): String {
        val rawData = StringBuilder(1024)
        var tempVariable: String?

        while (reader.readLine().also { tempVariable = it } != null) {
            rawData.append(tempVariable).append("\n")
        }

        reader.close()
        return rawData.toString()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}