package com.example.movieapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.crawler.MoviesService
import com.example.movieapp.crawler.TypeDisplay
import com.example.movieapp.crawler.pojo.Movie
import com.example.movieapp.crawler.pojo.ResultPage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */


const val URL = "https://api.themoviedb.org/3/movie/"

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    /**
     * Our 3 lists of Movies
     */
    private var currentList: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData<TypeDisplay>(TypeDisplay.POPULAR)

    init {
        // Change mean that we need to get another list
        typeDisplay.observeForever {
            changeValue()
        }
    }

    /*
    return the list of elements necessary
     */
    fun getListCurrent(): LiveData<List<Movie>> {
        return currentList
    }

    fun getList(typeDisplay: TypeDisplay) {
        this.typeDisplay.value = typeDisplay
    }

    private fun internetCall() {
        // prepare the internet call
        val service = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesService::class.java)

        typeDisplay.value?.s?.let {
            service.listOfMovies(it).enqueue(object : Callback<ResultPage> {
                override fun onResponse(call: Call<ResultPage>, response: Response<ResultPage>) {
                    val allData = response.body()
                    if (allData != null) {
                        Log.i(TAG, "onResponse: all elements are in the phone ")
                        for (c in allData.results)
                            Log.d(TAG, "onResponse:   $c ")
                        currentList.value = allData.results
                    }
                }

                override fun onFailure(call: Call<ResultPage>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }

    }

    private fun changeValue() {
        when (typeDisplay.value) {
            TypeDisplay.POPULAR -> {
                internetCall()
            }
            TypeDisplay.RATED -> {
                internetCall()
            }
            TypeDisplay.LIKED -> TODO("Room not implemented, so Likes doesn't exist")
        }
    }

    override fun onCleared() {
        super.onCleared()
        typeDisplay.removeObserver { }
    }
}