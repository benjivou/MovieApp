package com.example.movieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.data.dao.MovieDAO

import com.example.movieapp.data.entities.displayabledata.EmptyMoviePrepared
import com.example.movieapp.data.entities.displayabledata.ErrorMoviePrepared
import com.example.movieapp.data.entities.displayabledata.MoviePrepared
import com.example.movieapp.data.entities.displayabledata.SuccessMoviePrepared
import com.example.movieapp.data.entities.internet.ApiEmptyResponse
import com.example.movieapp.data.entities.internet.ApiErrorResponse
import com.example.movieapp.data.entities.internet.ApiSuccessResponse
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.util.Singleton.service

private const val TAG = "DetailViewModel"

class DetailViewModel : ViewModel() {

    private val movieDAO = MovieDAO()

    private var currentId: MutableLiveData<Int> = MutableLiveData()
    private var _currentMoviePair = MediatorLiveData<MoviePrepared<Pair<Movie, Boolean>>>()
    private var movieCurrent: LiveData<MoviePrepared<Pair<Movie, Boolean>>> =
        Transformations.switchMap(currentId) {
            it?.let { internetCall(it.toString()) }
        }

    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: $it")
            movieDAO.checkIfExist(it)
        }

    val currentMoviePair: LiveData<MoviePrepared<Pair<Movie, Boolean>>>
        get()  {
            init()
            return _currentMoviePair
        }


    private fun init() {

        _currentMoviePair.addSource(movieCurrent) { moviePrepared ->
            Log.d(TAG, "movieCurrent Modify ")
            if (moviePrepared is SuccessMoviePrepared)
                isLikedMovie.value?.let {
                    _currentMoviePair.value = SuccessMoviePrepared(
                        Pair(
                            moviePrepared.content.first,
                            it
                        )
                    )
                }
            else
                _currentMoviePair.value = moviePrepared
        }

        _currentMoviePair.addSource(isLikedMovie)
        { isLiked ->
            Log.d(TAG, "isLikedMLovie : Modified")
            val moviePrepared = _currentMoviePair.value
            if (moviePrepared is SuccessMoviePrepared)
                _currentMoviePair.value = SuccessMoviePrepared(
                    Pair(
                        moviePrepared.content.first,
                        isLiked
                    )
                )
        }
    }

    fun getMovieAndIsLiked(idMovie: Int) {
        currentId.value = idMovie
    }

    private fun internetCall(idMovie: String): LiveData<MoviePrepared<Pair<Movie, Boolean>>> {
        return Transformations.map(service.movieById(idMovie)) {
            when (it) {
                is ApiSuccessResponse -> SuccessMoviePrepared(Pair(it.body, false))
                is ApiEmptyResponse -> EmptyMoviePrepared<Pair<Movie, Boolean>>()
                is ApiErrorResponse -> ErrorMoviePrepared(
                    it.errorCode,
                    it.errorMessage
                )

            }
        }
    }

    fun likeOrUnlikeMovieExposed() {
        _currentMoviePair.value.let {
            if (it is SuccessMoviePrepared<Pair<Movie, Boolean>>)
                movieDAO.likeOrUnlikeMovie(it.content.first, isLikedMovie.value!!)

        }
    }
}