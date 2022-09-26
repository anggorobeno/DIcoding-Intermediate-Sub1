package com.example.core.data.source.user

import com.example.core.data.networkutils.BaseApiCall
import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.domain.model.user.LoginModel
import com.example.domain.repository.user.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.Dispatcher

class UserRepository(val dataSource: UserDataSource) : IUserRepository {
    override fun loginUser(body: LoginRequest): Flow<NetworkResult<LoginModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                dataSource.loginUser(body)
            }, { response ->
                LoginResponse.Transform(response!!)
            }))
        }.flowOn(Dispatchers.IO)
    }

}
