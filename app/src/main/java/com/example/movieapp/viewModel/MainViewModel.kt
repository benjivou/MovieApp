package com.example.movieapp.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.movieapp.model.Movie
import com.example.movieapp.App
import com.example.movieapp.model.TypeDisplay
import com.example.movieapp.viewModel.internetacces.ApiEmptyResponse
import com.example.movieapp.viewModel.internetacces.ApiErrorResponse
import com.example.movieapp.viewModel.internetacces.ApiSuccessResponse
import com.example.movieapp.viewModel.internetacces.MoviesService
import com.example.movieapp.viewModel.internetacces.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

const val URL = "https://api.themoviedb.org/3/movie/"


class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData(TypeDisplay.POPULAR)


    private var currentList =
        Transformations.switchMap<TypeDisplay, List<Movie>>(
            this.typeDisplay
        )
        {
            if (typeDisplay.value == TypeDisplay.LIKED)
                App.database.movieDAO().getAll()
            else
                internetCall()
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

    private fun internetCall(): LiveData<List<Movie>> {
        // prepare the internet call
        val service = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(MoviesService::class.java)

        return Transformations.map(service.listOfMovies(typeDisplay.value!!.s)) {
            when (it) {
                is ApiSuccessResponse -> it.body.results
                is ApiEmptyResponse -> emptyList()
                is ApiErrorResponse -> {
                    Log.w(TAG, "internetCall: " + it.errorMessage)
                    emptyList()
                }
            }
        }
    }
}