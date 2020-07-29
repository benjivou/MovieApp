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
import com.example.movieapp.ui.fragment.DetailFragment

private const val TAG = "DetailViewModel"

class DetailViewModel : AbstractViewModel(), DetailFragment.DetailFragmentListener {

    private var currentId: MutableLiveData<String?> = MutableLiveData()
    private var currentMoviePair = MediatorLiveData<Pair<Movie?, Boolean>>()
    private var movieCurrent = Transformations.switchMap(currentId) {
        it?.let { internetCall(it) } ?: null
    }
    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: ${it}")
            it?.toInt()?.let { it1 -> App.database.movieDAO().isLiked(it1) }
                ?: MutableLiveData()
        }

    init {
        currentMoviePair.addSource(movieCurrent) {
            currentMoviePair.value = Pair(it, isLikedMovie?.value ?: false)
        }
        currentMoviePair.addSource(isLikedMovie) { isLiked ->
            Log.i(TAG, "isLiked : modif de la list de movie ")
            currentMoviePair.value =
                Pair(movieCurrent.value, isLiked)
        }
    }

    fun getMovieAndIsLiked(idMovie: String): LiveData<Pair<Movie, Boolean>> {
        currentId.value = idMovie
        return currentMoviePair as LiveData<Pair<Movie, Boolean>>
    }

    private fun internetCall(idMovie: String): LiveData<Movie> {
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

    override fun onLikeButtonClicked() {
        Log.i(TAG, "onLikeButtonClicked: the content of the movie is :${movieCurrent.value} ")
        movieCurrent.value?.let { doOnLikePress(it) }
    }

    override fun doOnLikePress(movie: Movie) {
        Log.i(TAG, "doOnLikePress: click button pressed")
        if (isLikedMovie.value == true) {
            Log.i(TAG, "doOnLikePress: onLikeButtonClicked: delete ")
            deleteMovie(movie)
        } else {
            Log.i(TAG, "doOnLikePress: onLikeButtonClicked: insert ")
            insertMovie(movie)
        }
    }
}