package com.example.domain.usecase.user

import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.source.user.UserRepository
import com.example.domain.model.user.LoginModel
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val repository: UserRepository) : UserUseCase {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return repository.loginUser(body)
    }
}