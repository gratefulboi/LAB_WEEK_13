package com.example.lab_week_13

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.lab_week_13.api.MovieService
import com.example.lab_week_13.database.MovieDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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

        // Buat MovieDatabase instance
        val movieDatabase = MovieDatabase.getInstance(applicationContext)

        // Buat MovieRepository instance
        movieRepository = MovieRepository(movieService, movieDatabase)

        // Buat constraint instance
        val constraints = Constraints.Builder()
            // Hanya berjalanjika device terhubung ke internet
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Buat WorkRequest instance
        // Ini akan dipakai untuk menjadwalkan background task
        val workRequest = PeriodicWorkRequest
            // Jalankan task setiap 1 jam, bahkan ketika aplikasi ditutup atau di restart
            .Builder(
                MovieWorker::class.java, 1, TimeUnit.HOURS
            ).setConstraints(constraints)
            .addTag("movie-work").build()
        // Schedule the background task
        WorkManager.getInstance(
            applicationContext
        ).enqueue(workRequest)
    }
}