package com.example.core.data.source.user

import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.services.DicodingStoryApiService
import retrofit2.Response

class UserRemoteDataSourceImpl(val apiService: DicodingStoryApiService): UserDataSource {
    override suspend fun loginUser(body: LoginRequest): Response<LoginResponse> {
        return apiService.loginUser(body)
    }
}