package com.example.lab_week_13.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab_week_13.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    companion object {
        // Volatile berfungsi mencegah race condition
        // Jika thread lain sedang update database melalui instance, value dari instance akan terlihat oleh thread lain
        // Memastikan vale dari instance selalu up to date dan sama di semua execution thread
        @Volatile
        private var instance: MovieDatabase? = null
        fun getInstance(context: Context) : MovieDatabase {
            // Synchronized memastikan hanya satu thread yang dapat mengeksekusi block kode ini dalam 1 waktu
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(
                    context
                ).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context): MovieDatabase {
            return Room.databaseBuilder(
                context,
                MovieDatabase::class.java, "movie-db"
            ).build()
        }
    }
}