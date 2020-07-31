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
private const val TAG = "MainViewModel"

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

    private var movieList = Transformations.switchMap<TypeDisplay, List<MoviePrepared<Movie>>>(
        this.typeDisplay
    )
    {
        when (typeDisplay.value) {
            TypeDisplay.LIKED -> Transformations.map(App.database.movieDAO().getAll()) {
                it.map { movie -> SuccessMoviePrepared(movie, false) }
            }
            TypeDisplay.LIKED_POPULAR -> Transformations.map(App.database.movieDAO().getAll()) {
                it.map { movie -> SuccessMoviePrepared(movie, false) }
            }
            TypeDisplay.LIKED_RATED -> Transformations.map(App.database.movieDAO().getAll()) {
                it.map { movie -> SuccessMoviePrepared(movie, false) }
            }
            else -> internetCall()
        }

    }

    // List of Movies ready to be displayed
    private var _currentList = MediatorLiveData<List<MoviePrepared<Movie>>>()
    val currentList: LiveData<List<MoviePrepared<Movie>>>
        get() = _currentList
    init {
        _currentList.addSource(likedList) { listMovies ->
            val buf = mutableListOf<MoviePrepared<Movie>>()
            if (movieList.value?.get(0) is SuccessMoviePrepared<Movie>) {
                (movieList.value as List<SuccessMoviePrepared<Movie>>)?.forEach {
                    buf.add(SuccessMoviePrepared(it.body, listMovies.contains(it.body)))
                }
                _currentList.value = buf
            }

        }

        _currentList.addSource(movieList) { listMoviePrepared ->
            var buf = mutableListOf<MoviePrepared<Movie>>()
            if (listMoviePrepared.get(0) is SuccessMoviePrepared<Movie>) {
                (listMoviePrepared as List<SuccessMoviePrepared<Movie>>)?.forEach {
                    buf.add(SuccessMoviePrepared(it.body, likedList.value?.contains(it.body)?:false))
                }
            } else {
                buf.add(listMoviePrepared[0])
            }
            _currentList.value = buf
        }
    }



    fun getList(typeDisplay: TypeDisplay) {
        this.typeDisplay.value = typeDisplay
    }

    private fun internetCall(): LiveData<List<MoviePrepared<Movie>>> =
        Transformations.map(service.listOfMovies(typeDisplay.value!!.s)) {
            when (it) {
                is ApiSuccessResponse -> it.body.results.map { movie ->
                    SuccessMoviePrepared(
                        movie,
                        false
                    )
                }
                is ApiEmptyResponse -> {
                    val list = mutableListOf<MoviePrepared<Movie>>()
                    list.add(EmptyMoviePrepared())
                    list
                }
                is ApiErrorResponse -> {
                    val list = mutableListOf<MoviePrepared<Movie>>()
                    list.add(ErrorMoviePrepared(it.errorCode, it.errorMessage))
                    list
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
}