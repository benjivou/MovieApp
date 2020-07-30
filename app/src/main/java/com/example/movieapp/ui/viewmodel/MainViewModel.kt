package com.example.movieapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.movieapp.App
import com.example.movieapp.data.entities.ApiEmptyResponse
import com.example.movieapp.data.entities.ApiErrorResponse
import com.example.movieapp.data.entities.ApiSuccessResponse
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.TypeDisplay

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

const val URL = "https://api.themoviedb.org/3/movie/"
private const val TAG = "MainViewModel"

class MainViewModel : AbstractViewModel() {

    private val likedList = App.database.movieDAO().getAll()

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData(TypeDisplay.POPULAR)

    // List of raw Movies

    private var movieList = Transformations.switchMap<TypeDisplay, List<Movie>>(
        this.typeDisplay
    ) {
        when (typeDisplay.value) {
            TypeDisplay.LIKED -> App.database.movieDAO().getAll()
            TypeDisplay.LIKED_POPULAR -> App.database.movieDAO().getAllByPopular()
            TypeDisplay.LIKED_RATED -> App.database.movieDAO().getAllByRated()
            else -> internetCall()
        }

    }

    // List of Movies ready to be displayed
    private var currentList = MediatorLiveData<List<Pair<Movie, Boolean>>>()

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
                buffer.add(Pair(movie, likedList.value?.contains(movie) == true))
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

    override fun likeOrUnlikeMovie(movie: Movie) {
        if (likedList.value?.contains(movie) == true) {
            deleteMovie(movie)
        } else {
            insertMovie(movie)
        }
    }
}