package com.example.movieapp.crawler.pojo

import com.google.gson.annotations.SerializedName

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */
data class ServerRequestContent(
    val page: Int,
    @SerializedName("total_results")val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    val results: List<Movie>
)