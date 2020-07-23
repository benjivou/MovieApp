package com.example.movieapp.crawler

/**
 * Created by Benjamin Vouillon on 17,July,2020
 */

/***
 * Represent the type of movie list qe want for this call
 */
enum class TypeDisplay(val s: String) {
    POPULAR("popular"),
    RATED("top_rated"),
    LIKED("liked")
}
