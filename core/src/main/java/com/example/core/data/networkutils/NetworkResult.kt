package com.example.core.data.networkutils

sealed class NetworkResult< T>(
    val data: T? = null,
    val message: SingleEvent<String>? = null
) {



    class Success<T>(data: T?) : NetworkResult<T>(data)

    class Error<T>(message: SingleEvent<String>?) : NetworkResult<T>(null, message)

    class Loading<T> : NetworkResult<T>()


}