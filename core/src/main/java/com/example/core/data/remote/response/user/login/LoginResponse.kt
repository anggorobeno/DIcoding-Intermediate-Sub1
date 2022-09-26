package com.example.core.data.remote.response.user.login

import com.example.core.data.networkutils.Mappable
import com.example.core.data.remote.response.GenericStatusResponse
import com.example.domain.model.user.LoginModel
import retrofit2.Response

data class LoginResponse(
    val status: GenericStatusResponse,
    val userId: String? = null,
    val name: String? = null,
    val token: String? = null
):Mappable<LoginModel,LoginResponse> {

    companion object {
        fun Transform(dto: LoginResponse): LoginModel {
            return LoginModel(
                dto.status,
                dto.userId,
                dto.name,
                dto.token
            )
        }
    }



    override fun mapToModel(response: LoginResponse): LoginModel {
        return LoginModel(
            response.status,
            response.userId,
            response.name,
            response.token
        )
    }
}


