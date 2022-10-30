package com.example.core.data.source.user

import com.example.core.data.utils.BaseApiCall
import com.example.core.data.utils.Mapper.toModel
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.repository.user.IUserRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val dataSource: UserDataSource,
) : IUserRepository {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                dataSource.loginUser(body)
            }) { response ->
                response!!.toModel()
            })
        }.flowOn(Dispatchers.IO)
    }

    override fun registerUser(body: RegisterRequest): Flow<NetworkResult<RegisterModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                dataSource.registerUser(body)
            }) { response ->
                response!!.toModel()
            })
        }.flowOn(Dispatchers.IO)
    }


}
