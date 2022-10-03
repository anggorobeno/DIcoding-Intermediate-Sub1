package com.example.domain.model.user.register

data class RegisterRequest(
    private val name: String?,
    private val email: String?,
    private val password: String?
)