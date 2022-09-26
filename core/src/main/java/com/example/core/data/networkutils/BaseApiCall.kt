package com.example.core.data.networkutils

import com.example.core.data.remote.response.GenericStatusResponse
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object BaseApiCall {
    suspend fun <T, H> safeApiCall(
        apiCall: suspend () -> Response<T>,
        responseModel: (T?) -> H
    ): NetworkResult<H> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                val model = responseModel.invoke(body)
                body?.let {
                    return NetworkResult.Success(model)
                }
            } else {
                val error: GenericStatusResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    GenericStatusResponse()::class.java
                )
                error.message?.let {
                    return error(it)
                }
            }
            return error("${response.code()} : ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(SingleEvent(errorMessage))
}


