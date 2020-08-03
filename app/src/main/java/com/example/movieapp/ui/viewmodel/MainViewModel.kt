package com.example.movieapp.ui.viewmodel


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
import com.example.movieapp.data.model.TypeDisplay
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

const val URL = "https://api.themoviedb.org/3/movie/"

class MainViewModel : ViewModel() {

    private val likedList = App.database.movieDAO().getAll()

    private val service: MoviesService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .build()
        .create(MoviesService::class.java)

    /**
     * Type of the list displayed
     */
    private var typeDisplay: MutableLiveData<TypeDisplay> =
        MutableLiveData(TypeDisplay.POPULAR)

    // List of raw Movies
    // TODO remove isLiked by default
    private var movieList =
        Transformations.switchMap<TypeDisplay, MoviePrepared<List<Pair<Movie, Boolean>>>>(
            this.typeDisplay
        )
        {
            when (typeDisplay.value) {
                TypeDisplay.LIKED -> Transformations.map(App.database.movieDAO().getAll()) {
                    convertMoviesToSuccessMoviesPrepared(it)
                }
                TypeDisplay.LIKED_POPULAR -> Transformations.map(App.database.movieDAO().getAll()) {
                    convertMoviesToSuccessMoviesPrepared(it)
                }
                TypeDisplay.LIKED_RATED -> Transformations.map(App.database.movieDAO().getAll()) {
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

            val bufM = movieList.value!!
            if (bufM is SuccessMoviePrepared<List<Pair<Movie, Boolean>>>) {
                _currentList.value = SuccessMoviePrepared(bufM.content.map {
                    Pair(it.first, listMovies.contains(it.first))
                })
            }
        }

        _currentList.addSource(movieList) { listMoviePrepared ->
            var buf = mutableListOf<Pair<Movie, Boolean>>()
            _currentList.value =
                if (listMoviePrepared is SuccessMoviePrepared<List<Pair<Movie, Boolean>>>) {
                    SuccessMoviePrepared(listMoviePrepared.content.map {
                        Pair<Movie, Boolean>(it.first, it.second)
                    })
                } else {
                    listMoviePrepared
                }
        }
    }


    fun getList(typeDisplay: TypeDisplay) {
        this.typeDisplay.value = typeDisplay
    }

    private fun internetCall(): LiveData<MoviePrepared<List<Pair<Movie, Boolean>>>> =
        Transformations.map(service.listOfMovies(typeDisplay.value!!.s)) {
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
        if (likedList.value?.contains(movie) == true) {
            deleteMovie(movie)
        } else {
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

    private fun convertMoviesToSuccessMoviesPrepared(movies: List<Movie>): SuccessMoviePrepared<List<Pair<Movie, Boolean>>> {
        val res = mutableListOf<Pair<Movie, Boolean>>()
        movies.forEach { movie -> res.add(Pair(movie, true)) }
        return SuccessMoviePrepared(res.toList())
    }
}