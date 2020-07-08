package com.example.movieapp.crawler.pojo

import java.util.*

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */
data class Movie(
    val popularity: Float,
    val vote_count: Int,
    val video: Boolean,
    val poster_path: String,
    val id: Int,
    val adult:Boolean,
    val backdrop_path:String,
    val original_language: String,
    val original_title: String,
    val genreIds:List<Int>?,
    val title:String,
    val vote_average: Float,
    val overview: String,
    val release_date: String,
    val additionalProperties: Map<String, Any>
)