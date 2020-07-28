package com.example.movieapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.App
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.TypeDisplay
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import com.example.movieapp.data.entities.ApiEmptyResponse
import com.example.movieapp.data.entities.ApiErrorResponse
import com.example.movieapp.data.entities.ApiSuccessResponse
import com.example.movieapp.data.entities.MoviesService
import com.example.movieapp.ui.adapter.MovieViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

const val URL = "https://api.themoviedb.org/3/movie/"
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData(TypeDisplay.POPULAR)

    // List of raw Movies
    private var likedList = App.database.movieDAO().getAll()
    private var movieList = Transformations.switchMap<TypeDisplay, List<Movie>>(
        this.typeDisplay
    ) {
        if (typeDisplay.value == TypeDisplay.LIKED)
            App.database.movieDAO().getAll()
        else
            internetCall()
    }

    // List of Movies ready to be displayed
    private var currentList = MediatorLiveData<List<Pair<Movie, Boolean>>>()

    val itemLiked = object : MovieViewHolder.ItemLiked {
        override fun onItemLiked(movie: Movie) {
            if (likedList.value?.contains(movie) == true) {
                deleteMovie(movie)
            } else {
                insertMovie(movie)
            }
        }
    }

    init {
        currentList.addSource(likedList) { listMovies ->
            val buffer: MutableList<Pair<Movie, Boolean>> = mutableListOf()
            movieList.value?.forEach { movie ->
                buffer.add(Pair(movie, listMovies.contains(movie)))
            }
            currentList.value = buffer
        }

        currentList.addSource(movieList) { listMovies ->
            val buffer: MutableList<Pair<Movie, Boolean>> = mutableListOf()
            listMovies.forEach { movie ->
                buffer.add(Pair(movie, likedList.value?.contains(movie) ?: false))
            }
            currentList.value = buffer
        }
    }

    /*
    return the list of elements necessary
     */
    fun getListCurrent() = currentList as LiveData<List<Pair<Movie, Boolean>>>

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

    fun insertMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            App.database.movieDAO().insertMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            App.database.movieDAO().deleteMovie(movie)
        }
    }
}