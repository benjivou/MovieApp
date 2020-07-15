package com.example.movieapp.crawler.pojo

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */
@Entity
data class Movie(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: Int,
    @ColumnInfo(name = "popularity") @SerializedName("popularity") val popularity: Float,
    @ColumnInfo(name = "vote_pount") @SerializedName("vote_pount") val votePount: Int,
    @ColumnInfo(name = "video") @SerializedName("video") val video: Boolean,
    @ColumnInfo(name = "poster_path") @SerializedName("poster_path") val posterPath: String,
    @ColumnInfo(name = "adult") @SerializedName("adult") val adult: Boolean,
    @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "original_language") @SerializedName("original_language") val originalLanguage: String,
    @ColumnInfo(name = "original_title") @SerializedName("original_title") val originalTitle: String,
    @ColumnInfo(name = "genreIds") @SerializedName("genreIds") val genreIds: List<Int>?,
    @ColumnInfo(name = "title") @SerializedName("title") val title: String,
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") val voteAverage: Float,
    @ColumnInfo(name = "overview") @SerializedName("overview") val overview: String,
    @ColumnInfo(name = "release_date") @SerializedName("release_date") val releaseDate: String,
    @ColumnInfo(name = "image") @SerializedName("image") var Image: Bitmap?

)