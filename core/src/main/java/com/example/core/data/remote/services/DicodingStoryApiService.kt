package com.example.core.data.remote.services

import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DicodingStoryApiService {
    @POST("login")
    suspend fun loginUser(@Body requestBody: LoginRequest): Response<LoginResponse>

}