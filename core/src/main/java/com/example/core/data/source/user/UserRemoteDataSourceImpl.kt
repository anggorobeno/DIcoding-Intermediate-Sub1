package com.example.core.data.source.user

import com.example.domain.model.user.login.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.core.data.remote.services.DicodingStoryApiService
import com.example.domain.model.user.register.RegisterRequest
import retrofit2.Response

class UserRemoteDataSourceImpl(val apiService: DicodingStoryApiService): UserDataSource {
    override suspend fun loginUser(body: LoginRequest): Response<LoginResponse> {
        return apiService.loginUser(body)
    }

    override suspend fun registerUser(body: RegisterRequest): Response<RegisterResponse> {
        return apiService.registerUser(body)
    }
}