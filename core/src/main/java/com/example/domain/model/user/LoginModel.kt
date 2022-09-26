package com.example.domain.model.user

import com.example.core.data.remote.response.GenericStatusResponse

data class LoginModel(
    val status: GenericStatusResponse,
    val userId: String? = null,
    val name: String? = null,
    val token: String? = null
)
