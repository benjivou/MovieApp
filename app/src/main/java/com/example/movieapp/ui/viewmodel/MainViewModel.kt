package com.example.movieapp.ui.viewmodel


import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.data.entities.displayabledata.EmptyMoviePrepared
import com.example.movieapp.data.entities.displayabledata.ErrorMoviePrepared
import com.example.movieapp.data.entities.displayabledata.MoviePrepared
import com.example.movieapp.data.entities.displayabledata.SuccessMoviePrepared
import com.example.movieapp.data.entities.internet.ApiEmptyResponse
import com.example.movieapp.data.entities.internet.ApiErrorResponse
import com.example.movieapp.data.entities.internet.ApiSuccessResponse
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.model.TypeDisplay
import com.example.movieapp.data.util.Handler
import com.example.movieapp.data.util.Singleton.service


/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

const val URL = "https://api.themoviedb.org/3/movie/"
private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {


    val likedList = Handler.getAllMovies()

    /**
     * Type of the list displayed
     */
    private val _typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData(TypeDisplay.POPULAR)

    val currentTypeDisplay: TypeDisplay
        get() = _typeDisplay.value!!


    private var movieList =
        Transformations.switchMap<TypeDisplay, MoviePrepared<List<Pair<Movie, Boolean>>>>(
            this._typeDisplay
        )
        {
            when (_typeDisplay.value) {
                TypeDisplay.LIKED -> Transformations.map(Handler.getAllMovies()) {
                    convertMoviesToSuccessMoviesPrepared(it)
                }
                TypeDisplay.LIKED_POPULAR -> Transformations.map(Handler.getAllMovies()) {
                    convertMoviesToSuccessMoviesPrepared(it)
                }
                TypeDisplay.LIKED_RATED -> Transformations.map(Handler.getAllMovies()) {
                    convertMoviesToSuccessMoviesPrepared(it)
                }
                else -> internetCall()
            }
        }

    // List of Movies ready to be displayed
    private var _currentList =
        MediatorLiveData<MoviePrepared<List<Pair<Movie, Boolean>>>>()
    val currentList: LiveData<MoviePrepared<List<Pair<Movie, Boolean>>>>
        get() = _currentList

    init {
        _currentList.addSource(likedList) { listMovies ->

            val bufM = movieList.value
            if (bufM is SuccessMoviePrepared<List<Pair<Movie, Boolean>>>) {
                Log.i(TAG, "likedlist : $listMovies ")
                _currentList.value = SuccessMoviePrepared(bufM.content.map {
                    Pair(it.first, listMovies.contains(it.first))
                })
            }
        }

        _currentList.addSource(movieList) { listMoviePrepared ->
            _currentList.value =
                if (listMoviePrepared is SuccessMoviePrepared<List<Pair<Movie, Boolean>>>) {

                    SuccessMoviePrepared(listMoviePrepared.content.map {
                        Log.i(TAG, "movie :${it.first.originalTitle} ")
                        likedList.value!!.forEach { movie ->
                            Log.i(
                                TAG,
                                "content of the list : ${movie.originalTitle}"
                            )
                        }

                        Pair(it.first, likedList.value!!.contains(it.first))
                    })
                } else {
                    Log.i(TAG, "list movie is not a succesMoviePrepared: ")
                    listMoviePrepared
                }
        }
    }


    fun getList(typeDisplay: TypeDisplay) {
        this._typeDisplay.value = typeDisplay
    }

    private fun internetCall(): LiveData<MoviePrepared<List<Pair<Movie, Boolean>>>> =
        Transformations.map(service.listOfMovies(_typeDisplay.value!!.s)) {
            when (it) {
                is ApiSuccessResponse -> SuccessMoviePrepared(it.body.results.map { movie ->
                    Pair(movie, false)
                })
                is ApiEmptyResponse -> {
                    EmptyMoviePrepared<List<Pair<Movie, Boolean>>>()
                }
                is ApiErrorResponse -> {
                    ErrorMoviePrepared(it.errorCode, it.errorMessage)
                }
            }
        }


    fun likeOrUnlikeMovie(movie: Movie) {
        Handler.likeOrUnlikeMovie(
            movie,
            this.likedList.value!!.contains(movie)
        )
    }


    private fun convertMoviesToSuccessMoviesPrepared(movies: List<Movie>): SuccessMoviePrepared<List<Pair<Movie, Boolean>>> {
        val res = mutableListOf<Pair<Movie, Boolean>>()
        movies.forEach { movie -> res.add(Pair(movie, true)) }
        return SuccessMoviePrepared(res.toList())
    }
}