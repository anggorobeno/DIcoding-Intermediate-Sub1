package com.example.core.data.source.user

import com.example.domain.model.user.login.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.user.register.RegisterRequest
import retrofit2.Response

interface UserDataSource {
    suspend fun loginUser(body: LoginRequest): Response<LoginResponse>
    suspend fun registerUser(body: RegisterRequest): Response<RegisterResponse>
}