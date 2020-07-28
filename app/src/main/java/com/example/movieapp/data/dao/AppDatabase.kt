package com.example.movieapp.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.data.dao.MoviesDAO
import com.example.movieapp.data.model.Converters
import com.example.movieapp.data.model.Movie

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
@Database(entities = [Movie::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDAO(): MoviesDAO
}

