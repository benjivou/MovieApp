package com.example.movieapp.data.entities

import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.untracked.KEY_PRIVATE
import com.example.movieapp.data.model.ResultPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

interface MoviesService {

    @GET("{type}")
    fun listOfMovies(
        @Path("type") type: String,
        @Query("api_key") s: String = KEY_PRIVATE
    ): MutableLiveData<ApiResponse<ResultPage>>

    @GET("{idMovie}")
    fun movieById(
        @Path("idMovie") idMovie: String,
        @Query("api_key") s: String = KEY_PRIVATE
    ): MutableLiveData<ApiResponse<Movie>>
}

