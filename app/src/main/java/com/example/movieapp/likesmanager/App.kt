package com.example.movieapp.likesmanager

import android.app.Application
import androidx.room.Room

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
class App : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, "DBMovies").build()
    }
}