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

private const val TAG = "DetailViewModel"

// TODO afficher le movie n'a pas pu être trouver
// TODO le current id ne doit pas être null
class DetailViewModel : AbstractViewModel() {

    private var currentId: MutableLiveData<Int?> = MutableLiveData()
    private var currentMoviePair = MediatorLiveData<Pair<Movie?, Boolean>>()
    private var movieCurrent: LiveData<Movie?> = Transformations.switchMap(currentId) {
        it?.let { internetCall(it.toString()) } ?: null
    }
    private var isLikedMovie: LiveData<Boolean> =
        Transformations.switchMap(currentId) {
            Log.i(TAG, "getMovieAndIsLiked: $it")
            it?.let { it1 -> App.database.movieDAO().isLiked(it1) }
                ?: MutableLiveData()
        }

    init {

    }

    fun getMovieAndIsLiked(idMovie: Int): LiveData<Pair<Movie?, Boolean>> {
        currentId.value = idMovie
        currentMoviePair.addSource(movieCurrent) {
            currentMoviePair.value = Pair(it, isLikedMovie?.value ?: false)
        }
        currentMoviePair.addSource(isLikedMovie) { isLiked ->
            Log.i(TAG, "isLiked : modif de la list de movie ")
            currentMoviePair.value =
                Pair(movieCurrent.value, isLiked)
        }
        return currentMoviePair
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

    override fun likeOrUnlikeMovie(movie: Movie) {
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
}