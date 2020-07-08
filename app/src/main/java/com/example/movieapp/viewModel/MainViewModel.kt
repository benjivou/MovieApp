package com.example.movieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.movieapp.crawler.TypeList
import com.example.movieapp.crawler.internetCall
import com.example.movieapp.crawler.pojo.Movie

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

enum class TypeDisplay(){
    POPULAR,
    RATED,
    LIKED
}

class MainViewModel : ViewModel() {

    /**
     * Our 3 lists of Movies
     */
    var popular:MutableLiveData<List<Movie>> = internetCall(TypeList.POPULAR)
    var rated:MutableLiveData<List<Movie>> = internetCall(TypeList.HIGHEST_RATE)


    /**
     * Type of the list displayed
     */
    var TypeDisplay: TypeDisplay = com.example.movieapp.viewModel.TypeDisplay.POPULAR

    /*
    return the list of elements necessary
     */
    /*fun getListCurrent() : MutableLiveData<List<Movie>> = when(TypeDisplay){
        com.example.movieapp.viewModel.TypeDisplay.POPULAR-> popular
        com.example.movieapp.viewModel.TypeDisplay.RATED -> rated
        com.example.movieapp.viewModel.TypeDisplay.LIKED -> TODO()
    }*/




}