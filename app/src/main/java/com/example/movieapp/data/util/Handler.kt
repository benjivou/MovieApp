package com.example.movieapp.data.util

import com.example.movieapp.App
import com.example.movieapp.data.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Handler {
    companion object {
        fun insertMovie(movie: Movie, scope: CoroutineScope) {
            scope.launch(Dispatchers.IO) {
                App.database.movieDAO().insertMovie(movie)
            }
        }

        fun deleteMovie(movie: Movie, scope: CoroutineScope) {
            scope.launch(Dispatchers.IO) {
                App.database.movieDAO().deleteMovie(movie)
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