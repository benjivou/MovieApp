package com.example.movieapp.data.util

import com.example.movieapp.data.entities.internet.MoviesService
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import com.example.movieapp.ui.viewmodel.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Singleton{

    val service: MoviesService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
        .create(MoviesService::class.java)
}