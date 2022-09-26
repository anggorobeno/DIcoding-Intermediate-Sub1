package com.example.domain.repository.user

import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.domain.model.user.LoginModel
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>>
}
