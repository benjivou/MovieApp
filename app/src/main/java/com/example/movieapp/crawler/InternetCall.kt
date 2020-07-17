package com.example.movieapp.crawler

import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.crawler.pojo.ResultPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */


/***
 * Represent the type of movie list qe want for this call
 */
enum class TypeList(val s: String) {
    POPULAR("popular"),
    HIGHEST_RATE("top_rated")
}

private val TAG = "InternetCall"


const val URL = "https://api.themoviedb.org/3/movie/"

interface MoviesService {

    @GET("{type}")
    fun listOfMovies(
        @Path("type") type: String,
        @Query("api_key") s: String = KEY_PRIVATE
    ): Call<ResultPage>
}

fun internetCall(type: TypeList): List<Movie> {

    var call: ResultPage? = null

    runBlocking(Dispatchers.IO) {
        val service = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesService::class.java)

        val toto = service.listOfMovies(type.s).execute().body()

        call = toto

    }
    return call?.results ?: listOf()
}