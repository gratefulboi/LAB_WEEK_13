package com.example.lab_week_13

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieWorker(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {
    // doWork memanggil background thread, disini diletakkan kode yang ingin dirun
    override fun doWork(): Result {
        // get reference ke repository
        val movieRepository = (context as MovieApplication).movieRepository

        // Launch coroutine di IO thread
        CoroutineScope(Dispatchers.IO).launch {
            movieRepository.fetchMoviesFromNetwork()
        }
        return Result.success()
    }
}