package com.example.movieapp.data.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.movieapp.data.entities.wrapper.RealmLiveData
import com.example.movieapp.data.model.Movie
import io.realm.Realm

private const val TAG = "Handler"


class MovieDAO() {

    private fun insertMovie(movie: Movie) {
        Log.d(TAG, "insertMovie: inserting movie")
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(movie)
        realm.commitTransaction()

    }

    private fun deleteMovie(movie: Movie) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(Movie::class.java).equalTo("id", movie.id).findFirst()?.deleteFromRealm()
        realm.commitTransaction()

    }

    fun likeOrUnlikeMovie(movie: Movie, boolean: Boolean) {
        if (boolean) {
            deleteMovie(movie)
        } else {
            insertMovie(movie)
        }
    }

    fun getAllMovies(
    ): LiveData<List<Movie>> {// TODO is the realm instance closed at the end
        val realm = Realm.getDefaultInstance()
        return Transformations.map(
            RealmLiveData(
                realm.where(Movie::class.java)
                    .findAllAsync()
            )
        ) {
            it?.freeze()
        }

        // Async runs the fetch off the main thread, and returns
        // results as LiveData back on the main.
    }

    fun getAllByPopular(): LiveData<List<Movie>> = Transformations.map(getAllMovies()) {
        it.sortedByDescending { it.popularity }
    }

    fun getAllByRated(): LiveData<List<Movie>> = Transformations.map(getAllMovies()) {
        it.sortedByDescending { it.voteAverage }
    }

    fun checkIfExist(
        idMovie: Int
    ): LiveData<Boolean> { // TODO is the realm instance closed at the end
        val realm = Realm.getDefaultInstance()
        return Transformations.map(
            RealmLiveData<Movie>(
                realm.where(Movie::class.java)
                    .equalTo("id", idMovie)
                    .findAllAsync()
            )
        ) {
            Log.i(TAG, "checkIfExist: $it")
            !it.isNullOrEmpty()
        }
        // Async runs the fetch off the main thread, and returns
        // results as LiveData back on the main.
    }


}
