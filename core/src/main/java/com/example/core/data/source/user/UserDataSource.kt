package com.example.core.data.source.user

import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import retrofit2.Response

interface UserDataSource {
    suspend fun loginUser(body: LoginRequest): Response<LoginResponse>
}