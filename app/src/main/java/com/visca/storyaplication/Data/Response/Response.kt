package com.visca.storyaplication.Data.Response

sealed class Response<out R> private constructor() {
    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val error: String) : Response<Nothing>()
    object Loading : Response<Nothing>()
    object Empty : Response<Nothing>()
}