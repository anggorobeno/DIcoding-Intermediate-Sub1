package com.example.core.data.utils

import android.util.Log
import com.example.core.data.remote.response.GenericStatusResponse
import com.example.domain.utils.NetworkResult
import com.google.gson.Gson
import retrofit2.Response

object BaseApiCall {
    suspend fun <T, H> safeApiCall(
        apiCall: suspend () -> Response<T>,
        responseModel: (T?) -> H
    ): NetworkResult<H> {
        try {
            val response = apiCall()
            Log.d("TAG", "safeApiCall: ${response.code().toString()}")
            if (response.isSuccessful) {
                Log.d("TAG", "safeApiCall: ${response.body().toString()}")
                val body = response.body()
                val model = responseModel.invoke(body)
                Log.d("TAG", "safeApiCall: ${model.toString()}")
                body?.let {
                    return NetworkResult.Success(model)
                }
            } else {
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
            Log.d("TAG", "safeApiCall: ${e.message}")
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


