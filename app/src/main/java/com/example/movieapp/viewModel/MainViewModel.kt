package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.crawler.TypeList
import com.example.movieapp.crawler.internetCall
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

enum class TypeDisplay {
    POPULAR,
    RATED,
    LIKED
}

class MainViewModel : ViewModel() {


    /**
     * Our 3 lists of Movies
     */
    private var popular: LiveData<List<Movie>>? = null
    private var rated: LiveData<List<Movie>>? = null


    /**
     * Type of the list displayed
     */
    private var typeDisplay: TypeDisplay = TypeDisplay.POPULAR

    /*
    return the list of elements necessary
     */
    fun getListCurrent(): LiveData<List<Movie>> {
        return getList(typeDisplay)
    }


    fun getList(typeDisplay: TypeDisplay): LiveData<List<Movie>> {
        this.typeDisplay = typeDisplay
        return when (typeDisplay) {
            TypeDisplay.POPULAR -> {
                popular = internetCall(TypeList.POPULAR)
                popular
            }
            TypeDisplay.RATED -> {
                rated = internetCall(TypeList.HIGHEST_RATE)
                rated
            }
            TypeDisplay.LIKED -> TODO("Room not implemented, so Likes doesn't exist")
        }!!
    }

}