package com.example.core.data.remote.response.user.login

import com.example.core.data.remote.response.GenericStatusResponse
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("loginResult")
    val data: LoginItem? = null
): GenericStatusResponse() {
    data class LoginItem(
        val userId: String? = null,
        val name: String? = null,
        val token: String? = null
    ) {
    }
}


