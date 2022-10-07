package com.example.core.data.utils

import android.util.Log
import com.example.core.data.remote.response.GenericStatusResponse
import com.example.domain.utils.NetworkResult
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import retrofit2.Response

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
                    Timber.d {
                        "ApiCall Success and response is not empty"
                    }
                    return NetworkResult.Success(model)
                }
            } else {
                Timber.d {
                    "ApiCall failed"
                }
                val errorResponse: GenericStatusResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    GenericStatusResponse()::class.java
                )
                errorResponse.message?.let {
                    return error(it)
                }
            }
            return error("${response.code()} : ${response.message()}")
        } catch (e: Exception) {
            Timber.e(e) {
                e.message.toString()
            }
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(
            com.example.domain.utils.SingleEvent(
                errorMessage
            )
        )
}


