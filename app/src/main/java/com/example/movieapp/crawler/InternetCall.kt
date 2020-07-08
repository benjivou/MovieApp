package com.example.movieapp.crawler

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.crawler.pojo.AllData
import com.example.movieapp.crawler.pojo.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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


const val URL = "http://api.themoviedb.org/3/movie/"

interface MoviesService {

    @GET("{type}")
    fun listOfMovies(
        @Path("type") type: String,
        @Query("api_key") s: String = KEY_PRIVATE
    ): Call<AllData>
}

fun internetCall(type: TypeList): MutableLiveData<List<Movie>> {
    val moviesRes = MutableLiveData<List<Movie>>()

    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(MoviesService::class.java)

    service.listOfMovies(type.s)
        .enqueue(object : Callback<AllData> {
            override fun onResponse(call: Call<AllData>, response: Response<AllData>) {

                val allCourse = response.body()

                if (allCourse != null) {
                    Log.i(TAG, "onResponse: all elements are in the phone ")

                    for (c in allCourse.results)
                        Log.d(TAG, "onResponse:  one course : ${c.original_title} ")

                    moviesRes.value = allCourse.results
                }


            }

            override fun onFailure(call: Call<AllData>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")

            }
        })


    return moviesRes
}