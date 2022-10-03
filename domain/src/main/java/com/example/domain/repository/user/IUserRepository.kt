package com.example.domain.repository.user

import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>>
    fun registerUser(body: RegisterRequest): Flow<NetworkResult<RegisterModel>>
}
