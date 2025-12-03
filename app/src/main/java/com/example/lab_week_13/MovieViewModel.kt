package com.example.lab_week_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
//    init {
//        fetchPopularMovies()
//    }

    // define StateFlow untuk menggantikan LiveData
    // StateFlow itu observable Flow yang mengeluarkan state updates ke collectors
    // MutableStateFlow adalah StateFlow yang bisa dichange valuenya

    val popularMovies: StateFlow<List<Movie>> = movieRepository.fetchMovies()
        .map {movies ->
            // Filter, akan dijalankan setiap kali repository emit data
            movies.sortedByDescending { it.popularity}
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // fetch movie dari API
//    private fun fetchPopularMovies() {
//        // Jalankan coroutine di viewModelScope
//        // Dispatcher.io berarti coroutine akan berjalan di shared pool threads
//        viewModelScope.launch(Dispatchers.IO) {
//            movieRepository.fetchMovies().catch {
//                // catch terminal operator yang catch exceptions dari Flow
//                _error.value = "An exception occured: ${it.message}"
//            }.collect {
//                // collect terminal operator yang collect value dari flow
//                _popularMovies.value = it
//            }
//        }
//    }
}