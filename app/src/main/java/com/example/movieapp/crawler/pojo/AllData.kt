package com.example.movieapp.crawler.pojo

/**
 * Created by Benjamin Vouillon on 08,July,2020
 */
data class AllData(val page:Int, val total_results:Int, val total_pages:Int, val results: List<Movie>)