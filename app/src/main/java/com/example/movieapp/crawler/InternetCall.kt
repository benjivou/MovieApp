package com.example.movieapp.crawler

import androidx.lifecycle.MutableLiveData

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */


/***
 * Represent the type of movie list qe want for this call
 */
enum class TypeList(s: String) {
    POPULAR("popular"),
    HIGHEST_RATE("top_rated")
}

private val URL = "http://api.themoviedb.org/3/movie/"
private val KEY_API = "?api_key="

fun InternetCall(type:TypeList) {

}