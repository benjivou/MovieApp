package com.example.movieapp.data.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.movieapp.data.entities.wrapper.RealmLiveData
import com.example.movieapp.data.model.Movie
import io.realm.Realm
import io.realm.kotlin.where

private const val TAG = "Handler"

// todo NE PAS AVOIR DE STATIC
class Handler() {
    companion object {
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
                realm.copyFromRealm(it)
            }

            // Async runs the fetch off the main thread, and returns
            // results as LiveData back on the main.
        }

        fun checkIfExist(
            idMovie: Int
        ): LiveData<Boolean> { // TODO is the realm instance closed at the end
            val realm = Realm.getDefaultInstance()
            val res = MutableLiveData<Boolean>()
            val user: Movie? = realm.where<Movie>()
                .equalTo("id", idMovie)
                .findFirstAsync()?.freeze()
            Log.i(TAG, "checkIfExist: $user")
            res.value = user != null

            return res


            // Async runs the fetch off the main thread, and returns
            // results as LiveData back on the main.
        }

    }
}
