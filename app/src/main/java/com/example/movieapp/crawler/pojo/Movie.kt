package com.example.movieapp.crawler.pojo

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */
data class Movie(
    val popularity: Float,
    @SerializedName("vote_pount")val votePount: Int,
    val video: Boolean,
    @SerializedName("poster_path")val posterPath: String,
    val id: Int,
    val adult:Boolean,
    @SerializedName("backdrop_path")val backdropPath:String,
    @SerializedName("original_language")val originalLanguage: String,
    @SerializedName("original_title")val originalTitle: String,
    val genreIds:List<Int>?,
    val title:String,
    @SerializedName("vote_average")val voteAverage: Float,
    val overview: String,
    @SerializedName("release_date") val releaseDate: String,
    val additionalProperties: Map<String, Any>
)