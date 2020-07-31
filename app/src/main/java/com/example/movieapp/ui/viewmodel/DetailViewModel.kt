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

// TODO afficher le movie n'a pas pu être trouver
class DetailViewModel : ViewModel() {

    private var currentId: MutableLiveData<Int> = MutableLiveData()
    private var _currentMoviePair = MediatorLiveData<MoviePrepared<Movie>>()
    private var movieCurrent: LiveData<MoviePrepared<Movie>> =
        Transformations.switchMap(currentId) {
            it?.let { internetCall(it.toString()) } ?: null
        }
    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: $it")
            App.database.movieDAO().isLiked(it)
        }

    val currentMoviePair: LiveData<MoviePrepared<Movie>>
        get() = _currentMoviePair

    init {

        _currentMoviePair.addSource(movieCurrent) { moviePrepared ->
            Log.d(TAG, "movieCurrent Modify ")

            if (moviePrepared is SuccessMoviePrepared)
                _currentMoviePair.value = SuccessMoviePrepared(
                    moviePrepared.body,
                    isLikedMovie.value!!
                )
            else {
                _currentMoviePair.value = moviePrepared
            }
        }

        _currentMoviePair.addSource(isLikedMovie)
        { isLiked ->
            Log.d(TAG, "isLikedMLovie : Modified")
            if (_currentMoviePair.value is SuccessMoviePrepared)
                _currentMoviePair.value = SuccessMoviePrepared(
                    (_currentMoviePair.value as SuccessMoviePrepared<Movie>).body,
                    isLiked
                )
        }
    }

    private val service: MoviesService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
        .create(MoviesService::class.java)


    fun getMovieAndIsLiked(idMovie: Int) {
        currentId.value = idMovie
    }

    private fun internetCall(idMovie: String): LiveData<MoviePrepared<Movie>> {
        return Transformations.map(service.movieById(idMovie)) {
            when (it) {
                is ApiSuccessResponse -> SuccessMoviePrepared(it.body, false)
                is ApiEmptyResponse -> EmptyMoviePrepared<Movie>()
                is ApiErrorResponse -> ErrorMoviePrepared(it.errorCode, it.errorMessage)
            }
        }
    }

    fun likeOrUnlikeMovieExposed() {
        _currentMoviePair?.value?.let {
            if (it is SuccessMoviePrepared<Movie>)
                likeOrUnlikeMovie(it.body)
        }
    }

    private fun likeOrUnlikeMovie(movie: Movie) {
        Log.i(com.example.movieapp.ui.viewmodel.TAG, "doOnLikePress: click button pressed")
        if (isLikedMovie.value == true) {
            Log.i(
                com.example.movieapp.ui.viewmodel.TAG,
                "doOnLikePress: onLikeButtonClicked: delete "
            )
            deleteMovie(movie)
        } else {
            Log.i(
                com.example.movieapp.ui.viewmodel.TAG,
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