package com.example.movieapp.likesmanager

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.model.Converters
import com.example.movieapp.model.Movie

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
@Database(entities = [Movie::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDAO(): MoviesDAO
}