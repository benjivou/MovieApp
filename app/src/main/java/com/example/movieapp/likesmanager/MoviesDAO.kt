package com.example.movieapp.likesmanager

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Update
    fun updateMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("Select * From Movie")
    fun getAll(): LiveData<List<Movie>>

    @Query("Select  Exists(Select * From Movie m Where m.id = :id ) ")
    fun isLiked(id: Int): LiveData<Boolean>
}