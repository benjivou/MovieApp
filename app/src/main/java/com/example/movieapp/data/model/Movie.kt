package com.example.movieapp.data.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


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
    @ColumnInfo(name = "genreIds") @TypeConverters(Converters::class) @SerializedName("genreIds") val genreIds: List<Int>?,
    @ColumnInfo(name = "title") @SerializedName("title") val title: String,
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") val voteAverage: Float,
    @ColumnInfo(name = "overview") @SerializedName("overview") val overview: String,
    @ColumnInfo(name = "release_date") @SerializedName("release_date") val releaseDate: String
)

class Converters {
    @TypeConverter
    fun listToString(value: List<Int>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun dateToTimestamp(value: String?): List<Int>? {
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}