package com.example.core.data.remote.response.user.login

import com.example.core.data.networkutils.Mappable
import com.example.core.data.remote.response.GenericStatusResponse
import com.example.domain.model.user.login.LoginModel

data class LoginResponse(
    val status: GenericStatusResponse? = null,
    val userId: String? = null,
    val name: String? = null,
    val token: String? = null
) : Mappable<LoginResponse, LoginModel> {

    companion object {
        fun Transform(dto: LoginResponse): LoginModel {
            return LoginModel(
                GenericStatusResponse.Transform(dto.status!!),
                dto.userId,
                dto.name,
                dto.token
            )
        }
    }

    override fun mapToModel(response: LoginResponse): LoginModel {
        return LoginModel(
            GenericStatusResponse.Transform(response.status!!),
            response.userId,
            response.name,
            response.token
        )
    }
}


