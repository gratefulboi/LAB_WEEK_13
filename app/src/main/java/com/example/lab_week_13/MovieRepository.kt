package com.example.lab_week_13

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lab_week_13.api.MovieService
import com.example.lab_week_13.database.MovieDao
import com.example.lab_week_13.database.MovieDatabase
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn


class MovieRepository(private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {


    private val apiKey = "56730abb6537da79713ce74cc21399b9"

    // LiveData yang berisi list movies
    private val movieLiveData = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = movieLiveData

    // LiveData yang berisi error message
    private val errorLiveData = MutableLiveData<String>()
    val error: LiveData<String>
        get() = errorLiveData

    // fetch movies from API
    // function ini return Flow dari movie objects
    fun fetchMovies(): Flow<List<Movie>> {
       return flow {
           // Check jika ada movies disimpan di database
           val movieDao: MovieDao = movieDatabase.movieDao()
           val savedMovies = movieDao.getMovies()

           // Jika gada movie di database, fetch popular movies dari API
           if(savedMovies.isEmpty()) {
               val movies = movieService.getPopularMovies(apiKey).results

               // Simpen list popular movies ke database
               movieDao.addMovies(movies)

               // emit list popular movies dari API
               emit(movies)
           } else {
               // Jika ada movies tersimpan di database, maka emit list saved movie dari database
               emit(savedMovies)
           }

           // Pake dispatcher.io untuk menjalankan coroutine ini di sharedpool of threads
       }.flowOn(Dispatchers.IO)
    }

    // Fetch movie dari API dan save di database. Function ini dipakai setiap interval untuk refresh list of popular movies
    suspend fun fetchMoviesFromNetwork() {
        val movieDao: MovieDao = movieDatabase.movieDao()
        try {
            val popularMovies = movieService.getPopularMovies(apiKey)
            val moviesFetched = popularMovies.results
            movieDao.addMovies(moviesFetched)
        } catch (exception: Exception) {
            Log.d(
                "MovieRepository",
                "An error occured: ${exception.message}"
            )
        }
    }
}