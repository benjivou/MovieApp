package com.example.movieapp

import android.app.Application
import androidx.room.Room
import com.example.movieapp.data.dao.AppDatabase

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
class App : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, "DBMovies")
            .fallbackToDestructiveMigration().build()
    }
}