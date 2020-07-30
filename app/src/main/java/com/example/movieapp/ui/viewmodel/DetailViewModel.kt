package com.example.movieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.movieapp.App
import com.example.movieapp.data.entities.ApiEmptyResponse
import com.example.movieapp.data.entities.ApiErrorResponse
import com.example.movieapp.data.entities.ApiSuccessResponse
import com.example.movieapp.data.entities.MoviesService
import com.example.movieapp.data.model.Movie
import com.example.movieapp.ui.livedata.LiveDataCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "DetailViewModel"

// TODO afficher le movie n'a pas pu être trouver
// TODO le current id ne doit pas être null
class DetailViewModel : ViewModel() {

    private var currentId: MutableLiveData<Int?> = MutableLiveData()
    private var _currentMoviePair = MediatorLiveData<Pair<Movie?, Boolean>>()
    private var movieCurrent: LiveData<Movie?> = Transformations.switchMap(currentId) {
        it?.let { internetCall(it.toString()) } ?: null
    }
    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: $it")
            it?.let { it1 -> App.database.movieDAO().isLiked(it1) }
                ?: MutableLiveData()
        }

    val currentMoviePair: LiveData<Pair<Movie?, Boolean>>
        get() = _currentMoviePair

    init {
        _currentMoviePair.addSource(movieCurrent) {
            _currentMoviePair.value = Pair(it, isLikedMovie?.value ?: false)
        }
        _currentMoviePair.addSource(isLikedMovie) { isLiked ->
            Log.i(TAG, "isLiked : modif de la list de movie ")
            _currentMoviePair.value =
                Pair(movieCurrent.value, isLiked)
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

    fun likeOrUnlikeMovieExposed() {
        movieCurrent?.let {
            likeOrUnlikeMovie(it.value!!)
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