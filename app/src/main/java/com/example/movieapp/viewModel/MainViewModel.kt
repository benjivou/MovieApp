package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.crawler.TypeList
import com.example.movieapp.crawler.internetCall
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

enum class TypeDisplay() {
    POPULAR,
    RATED,
    LIKED
}

class MainViewModel : ViewModel() {

    // TO-DO changer en LiveData + LAteinit à effacer remplacer par des nullables
    // TO-DO destroy adapter dans les onDestroy
    /**
     * Our 3 lists of Movies
     */
    var popular: LiveData<List<Movie>>? = null
    var rated: LiveData<List<Movie>>? = null


    /**
     * Type of the list displayed
     */
    var typeDisplay: TypeDisplay = com.example.movieapp.viewModel.TypeDisplay.POPULAR

    /*
    return the list of elements necessary
     */
    fun getListCurrent(): LiveData<List<Movie>> {
        return getList(typeDisplay)
    }

    fun getList(typeDisplay: TypeDisplay): LiveData<List<Movie>> {
        this.typeDisplay = typeDisplay
        return when (typeDisplay) {
            com.example.movieapp.viewModel.TypeDisplay.POPULAR -> {
                if (popular == null) popular = internetCall(TypeList.POPULAR)
                popular
            }
            com.example.movieapp.viewModel.TypeDisplay.RATED -> {
                if (rated == null) rated = internetCall(TypeList.HIGHEST_RATE)
                rated
            }
            com.example.movieapp.viewModel.TypeDisplay.LIKED -> TODO()
        }!!
    }
    // TO-DO regarder les viewmodels pour en avoir 1 différents remplacer par Intent qui passe l'id
}