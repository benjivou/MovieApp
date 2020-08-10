package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey



/**
 * Created by Benjamin Vouillon on 08,July,2020
 */

open class Movie(
    @PrimaryKey @SerializedName("id") var id: Int? = null,
    @SerializedName("popularity") var popularity: Float? = null,
    @SerializedName("vote_pount") var votePount: Int? = null,
    @SerializedName("video") var video: Boolean? = null,
    @SerializedName("poster_path") var posterPath: String? = null,
    @SerializedName("adult") var adult: Boolean? = null,
    @SerializedName("backdrop_path") var backdropPath: String? = null,
    @SerializedName("original_language") var originalLanguage: String? = null,
    @SerializedName("original_title") var originalTitle: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("vote_average") var voteAverage: Float? = null,
    @SerializedName("overview") var overview: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null
) : RealmObject() {

    override fun equals(other: Any?): Boolean {
        if (other is Movie)
            if (id == other.id)
                return true
        return super.equals(other)
    }

}

