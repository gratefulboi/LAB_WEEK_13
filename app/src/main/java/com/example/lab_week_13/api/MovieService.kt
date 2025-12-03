package com.example.lab_week_13.api

import com.example.lab_week_13.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET ("movie/popular")
    // Menggunakan suspend keyword untuk memberitau fungsi ini adalah coroutine
    // Suspend function bisa di pause dan dilanjutkan di lain waktu
    // Berguna untuk network calls, karena membutuhkan waktu lama untuk menyelesaikan dan kita gamau memblock main thread
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
    ) : PopularMoviesResponse
}