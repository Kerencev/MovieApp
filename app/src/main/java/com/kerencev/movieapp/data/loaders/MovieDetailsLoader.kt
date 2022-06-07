package com.kerencev.movieapp.data.loaders

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

/**
 * Класс для загрузки деталей фильма по его ID через API
 */

object MovieDetailsLoader {
    fun loadDetails(id: String): MovieDetailsApi? {
        val uri = URL("https://imdb-api.com/ru/API/Title/$API_KEY/$id")
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

            Gson().fromJson(lines, MovieDetailsApi::class.java)
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