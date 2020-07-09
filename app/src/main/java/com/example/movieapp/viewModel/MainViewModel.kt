package com.example.movieapp.viewModel

import androidx.lifecycle.MutableLiveData
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

    /**
     * Our 3 lists of Movies
     */
    var popular: MutableLiveData<List<Movie>>? = null
    var rated: MutableLiveData<List<Movie>>? = null


    /**
     * Type of the list displayed
     */
    var TypeDisplay: TypeDisplay = com.example.movieapp.viewModel.TypeDisplay.POPULAR

    /*
    return the list of elements necessary
     */
    fun getListCurrent(): MutableLiveData<List<Movie>> {
        return when (TypeDisplay) {
            com.example.movieapp.viewModel.TypeDisplay.POPULAR -> {
                if (popular== null ) popular = internetCall(TypeList.POPULAR)
                popular
            }
            com.example.movieapp.viewModel.TypeDisplay.RATED -> {
                if (rated== null ) rated = internetCall(TypeList.HIGHEST_RATE)
                rated
            }
            com.example.movieapp.viewModel.TypeDisplay.LIKED -> TODO()
        }!!
    }


}