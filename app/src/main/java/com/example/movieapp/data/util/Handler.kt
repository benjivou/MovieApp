package com.example.movieapp.data.util

import com.example.movieapp.data.model.Movie
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Handler {
    companion object {
        private fun insertMovie(movie: Movie, scope: CoroutineScope) {
            scope.launch(Dispatchers.IO) {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction { realm ->
                    val myMovie = realm.createObject<Movie>(Movie::class.java, movie) // TODO Test
                    realm.close()
                }
            }
        }

        private fun deleteMovie(movie: Movie, scope: CoroutineScope) {
            scope.launch(Dispatchers.IO) {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction { realm ->
                    val myMovie = realm.createObject<Movie>(Movie::class.java, movie) // TODO Test
                    myMovie.deleteFromRealm()
                    realm.close()
                }
            }
        }

        fun likeOrUnlikeMovie(movie: Movie, scope: CoroutineScope, boolean: Boolean) {
            if (boolean) {
                deleteMovie(movie, scope)
            } else {
                insertMovie(movie, scope)
            }
        }
    }
}
