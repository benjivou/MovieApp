package com.example.movieapp.data.entities.displayabledata

import com.example.movieapp.data.entities.internet.ApiEmptyResponse
import com.example.movieapp.data.entities.internet.ApiErrorResponse
import com.example.movieapp.data.entities.internet.ApiResponse
import com.example.movieapp.data.entities.internet.ApiSuccessResponse

sealed class MoviePrepared<T>() {
    companion object {
        fun <T> create(response: ApiResponse<T>, isLiked: Boolean): MoviePrepared<T> {
            return when (response) {
                is ApiEmptyResponse -> EmptyMoviePrepared()
                is ApiErrorResponse -> ErrorMoviePrepared(response.errorCode, response.errorMessage)
                is ApiSuccessResponse -> SuccessMoviePrepared(response.body, isLiked)
            }
        }
    }
}

class EmptyMoviePrepared<T> : MoviePrepared<T>()
data class ErrorMoviePrepared<T>(val errorCode: Int, val errorMessage: String) : MoviePrepared<T>()
data class SuccessMoviePrepared<T>(val body: T, val isLiked: Boolean) : MoviePrepared<T>()