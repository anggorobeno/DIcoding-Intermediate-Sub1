package com.example.submission1androidintermediate.usecase.user

import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.TestHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SuccessUserUseCase : UserUseCase {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return flow {
            emit(
                NetworkResult.Success(
                    TestHelper.provideSuccessLoginModel()
                )
            )
        }
    }

    override fun registerUser(body: RegisterRequest): Flow<NetworkResult<RegisterModel>> {
        return flow {
            emit(
                NetworkResult.Success(
                    TestHelper.provideSuccessRegisterModel()
                )
            )
        }
    }
}