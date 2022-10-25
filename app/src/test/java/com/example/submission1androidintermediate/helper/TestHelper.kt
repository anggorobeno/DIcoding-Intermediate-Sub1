package com.example.submission1androidintermediate.helper

import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest

object TestHelper {
    // RegisterViewModel
    fun provideSuccessRegisterModel(): RegisterModel {
        return RegisterModel(
            false,
            "No Error",
        )
    }

    fun provideErrorRegisterModel() = RegisterModel(true,"Network is error")

    fun provideRegisterRequest() = RegisterRequest(
        "Anggoro",
        "anggoro@gmail.com",
        "testaja"
    )

}