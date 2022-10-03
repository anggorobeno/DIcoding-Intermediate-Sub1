package com.example.core.data.remote.services

import com.example.domain.model.user.login.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.user.register.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DicodingStoryApiService {
    @POST("login")
    suspend fun loginUser(@Body requestBody: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body requestBody: RegisterRequest): Response<RegisterResponse>
}