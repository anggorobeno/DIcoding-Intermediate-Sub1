package com.example.domain.model.user.login

import com.example.domain.model.GenericStatusModel

data class LoginModel(
    val status: GenericStatusModel?,
    val userId: String? = null,
    val name: String? = null,
    val token: String? = null
)
