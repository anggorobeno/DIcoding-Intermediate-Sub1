package com.example.domain.usecase.user

import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.domain.model.user.LoginModel
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>>

}
