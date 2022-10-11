package com.example.domain.model.user.login

data class LoginModel(
    val error: Boolean?,
    val message: String?,
    val data: LoginItemModel?
) {
    data class LoginItemModel(
        val userId: String? = null,
        val name: String? = null,
        val token: String? = null
    )
}
