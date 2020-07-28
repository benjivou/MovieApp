package com.example.movieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.entities.ApiEmptyResponse
import com.example.movieapp.data.entities.ApiErrorResponse
import com.example.movieapp.data.entities.ApiSuccessResponse
import com.example.movieapp.data.entities.MoviesService
import com.example.movieapp.data.model.Movie
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "DetailViewModel"

class DetailViewModel : ViewModel() {

     fun internetCall(idMovie: String): LiveData<Movie> {
        // prepare the internet call
        val service = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(MoviesService::class.java)

        return Transformations.map(service.movieById(idMovie)) {
            when (it) {
                is ApiSuccessResponse -> it.body
                is ApiEmptyResponse -> null
                is ApiErrorResponse -> {
                    Log.w(TAG, "internetCall: " + it.errorMessage)
                    null
                }
            }
        }
    }
}