package com.example.movieapp.crawler.pojo

import android.R.attr.data
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


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
    @ColumnInfo(name = "release_date") @SerializedName("release_date") val releaseDate: String,
    @ColumnInfo(
        name = "image",
        typeAffinity = ColumnInfo.BLOB
    ) @SerializedName("image") var image: ByteArray? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (popularity != other.popularity) return false
        if (votePount != other.votePount) return false
        if (video != other.video) return false
        if (posterPath != other.posterPath) return false
        if (adult != other.adult) return false
        if (backdropPath != other.backdropPath) return false
        if (originalLanguage != other.originalLanguage) return false
        if (originalTitle != other.originalTitle) return false
        if (genreIds != other.genreIds) return false
        if (title != other.title) return false
        if (voteAverage != other.voteAverage) return false
        if (overview != other.overview) return false
        if (releaseDate != other.releaseDate) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image!!.contentEquals(other.image!!)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + popularity.hashCode()
        result = 31 * result + votePount
        result = 31 * result + video.hashCode()
        result = 31 * result + posterPath.hashCode()
        result = 31 * result + adult.hashCode()
        result = 31 * result + backdropPath.hashCode()
        result = 31 * result + originalLanguage.hashCode()
        result = 31 * result + originalTitle.hashCode()
        result = 31 * result + genreIds.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + voteAverage.hashCode()
        result = 31 * result + overview.hashCode()
        result = 31 * result + releaseDate.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}


class Converters {
    @TypeConverter
    fun listToString(value: List<Int>?): String? {

        return Gson().toJson( value )
    }

    @TypeConverter
    fun dateToTimestamp(value: String?): List<Int>? {
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value,listType)
    }
}