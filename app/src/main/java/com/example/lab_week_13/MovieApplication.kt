package com.example.lab_week_13

import android.app.Application
import com.example.lab_week_13.api.MovieService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        // Buat instance Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        // buat MovieService instance dan bind MovieService interface dengan Retrofit instance
        // Berfungsi untuk melakukan API calls
        val movieService = retrofit.create(
            MovieService::class.java
        )

        // Buat MovieRepository instance
        movieRepository = MovieRepository(movieService)
    }
}