package com.kerencev.movieapp.data.loaders

import com.kerencev.movieapp.data.entities.details.MovieDetailsApi
import com.kerencev.movieapp.data.entities.list.MoviesListApi
import com.kerencev.movieapp.data.entities.name.NameData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieLoaderRetrofit {

    @GET("/ru/API/Title/$API_KEY/{id}")
    fun getMovieDetails(@Path ("id") id: String) : Call<MovieDetailsApi>

    @GET("/ru/API/{category}/$API_KEY")
    fun getMovies(@Path ("category") category: String) : Call<MoviesListApi>

    @GET("/ru/API/Name/$API_KEY/{id}")
    fun getNameData(@Path ("id") id: String) : Call<NameData>

    companion object {
        private const val API_KEY = "k_62zj7tzu"
        private const val BASE_URL = "https://imdb-api.com/"

        fun create() : MovieLoaderRetrofit {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(MovieLoaderRetrofit::class.java)
        }
    }
}