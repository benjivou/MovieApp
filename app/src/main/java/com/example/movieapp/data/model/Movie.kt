package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

data class Movie(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("popularity") val popularity: Float,
    @SerializedName("vote_pount") val votePount: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("genreIds") val genreIds: List<Int>?,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String
) : RealmObject()
