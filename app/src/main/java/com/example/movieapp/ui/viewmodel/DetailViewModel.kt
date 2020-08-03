package com.example.movieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.App
import com.example.movieapp.data.entities.displayabledata.EmptyMoviePrepared
import com.example.movieapp.data.entities.displayabledata.ErrorMoviePrepared
import com.example.movieapp.data.entities.displayabledata.MoviePrepared
import com.example.movieapp.data.entities.displayabledata.SuccessMoviePrepared
import com.example.movieapp.data.entities.internet.ApiEmptyResponse
import com.example.movieapp.data.entities.internet.ApiErrorResponse
import com.example.movieapp.data.entities.internet.ApiSuccessResponse
import com.example.movieapp.data.entities.internet.MoviesService
import com.example.movieapp.data.model.Movie
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "DetailViewModel"

class DetailViewModel : ViewModel() {

    private var currentId: MutableLiveData<Int> = MutableLiveData()
    private var _currentMoviePair = MediatorLiveData<MoviePrepared<Pair<Movie, Boolean>>>()
    private var movieCurrent: LiveData<MoviePrepared<Pair<Movie, Boolean>>> =
        Transformations.switchMap(currentId) {
            it?.let { internetCall(it.toString()) }
        }
    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: $it")
            App.database.movieDAO().isLiked(it)
        }

    val currentMoviePair: LiveData<MoviePrepared<Pair<Movie, Boolean>>>
        get() = _currentMoviePair


    init {

        // TODO moviePrepared.isLiked = isLikedMovie
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


        // TODO verify if I neeed to recreate another SucessMoviePrepared
        _currentMoviePair.addSource(isLikedMovie)
        { isLiked ->
            Log.d(TAG, "isLikedMLovie : Modified")
            val toto = _currentMoviePair.value
            if (toto is SuccessMoviePrepared)
                _currentMoviePair.value = SuccessMoviePrepared(
                    Pair(
                        toto.content.first,
                        isLiked
                    )
                )
        }
    }

    // TODO (reuse thinks ) Or Singleton ( instate just 1 time )
    private val service: MoviesService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
        .create(MoviesService::class.java)


    fun getMovieAndIsLiked(idMovie: Int) {
        currentId.value = idMovie
    }

    private fun internetCall(idMovie: String): LiveData<MoviePrepared<Pair<Movie, Boolean>>> {
        return Transformations.map(service.movieById(idMovie)) {
            when (it) {
                is ApiSuccessResponse -> SuccessMoviePrepared(Pair(it.body, false))
                is ApiEmptyResponse -> EmptyMoviePrepared<Pair<Movie, Boolean>>()
                is ApiErrorResponse -> ErrorMoviePrepared(it.errorCode, it.errorMessage)
            }
        }
    }

    fun likeOrUnlikeMovieExposed() {
        _currentMoviePair.value.let {
            if (it is SuccessMoviePrepared<Pair<Movie, Boolean>>)
                likeOrUnlikeMovie(it.content.first)
        }
    }

    // TODO handler for insertMovie/deleteMovie/LikeOrUnlikeMovie
    private fun likeOrUnlikeMovie(movie: Movie) {
        Log.i(TAG, "doOnLikePress: click button pressed")
        if (isLikedMovie.value == true) {
            Log.i(
                TAG,
                "doOnLikePress: onLikeButtonClicked: delete "
            )
            deleteMovie(movie)
        } else {
            Log.i(
                TAG,
                "doOnLikePress: onLikeButtonClicked: insert "
            )
            insertMovie(movie)
        }
    }

    private fun insertMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            App.database.movieDAO().insertMovie(movie)
        }
    }

    private fun deleteMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            App.database.movieDAO().deleteMovie(movie)
        }
    }
}