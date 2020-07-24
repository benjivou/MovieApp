package com.example.movieapp.viewModel.internetacces

import androidx.lifecycle.MutableLiveData
import com.example.movieapp.viewModel.room.KEY_PRIVATE
import com.example.movieapp.model.ResultPage
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
}

