package com.example.movieapp.likesmanager

import android.view.View
import androidx.lifecycle.LiveData
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
class LikeClicker(val movie: Movie) : View.OnClickListener {

    private val liked: LiveData<Boolean> = App.database.movieDAO().isLiked(movie.id)

    override fun onClick(v: View?) {
        // if the check is done ( so liked not null )
        liked.value?.let {
            // if the object is already liked
            if (it)
                App.database.movieDAO().deleteMovie(movie)
            // if the object is not liked
            else
                App.database.movieDAO().insertMovie(movie)
        }


    }

}