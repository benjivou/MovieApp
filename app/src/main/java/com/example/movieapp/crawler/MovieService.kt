package com.example.movieapp.crawler

import com.example.movieapp.crawler.pojo.ResultPage
import retrofit2.Call
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
    ): Call<ResultPage>
}

