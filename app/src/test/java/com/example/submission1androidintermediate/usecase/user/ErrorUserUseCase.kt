package com.example.submission1androidintermediate.usecase.user

import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ErrorUserUseCase : UserUseCase {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return flow {
            emit(
                NetworkResult.Error(
                    SingleEvent("network is error")
                )
            )
        }
    }

    override fun registerUser(body: RegisterRequest): Flow<NetworkResult<RegisterModel>> {
        return flow {
            emit(
                NetworkResult.Error(
                    SingleEvent("network is error")
                )
            )
        }
    }
}