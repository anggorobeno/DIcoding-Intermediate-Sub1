package com.example.domain.usecase.user

import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.repository.user.IUserRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val repository: IUserRepository) : UserUseCase {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return repository.loginUser(body)
    }

    override fun registerUser(body: RegisterRequest): Flow<NetworkResult<RegisterModel>> {
        return repository.registerUser(body)
    }
}