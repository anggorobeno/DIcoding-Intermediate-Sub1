package com.example.core.data.utils

import com.example.core.data.remote.response.GenericStatusResponse
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.GenericStatusModel
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.register.RegisterModel

object Mapper {
    fun GenericStatusResponse.toModel(): GenericStatusModel {
        return GenericStatusModel(
            this.error,
            this.message
        )
    }

    fun RegisterResponse.toModel(): RegisterModel {
        return RegisterModel(
            this.error,
            this.message
        )
    }

    fun LoginResponse.LoginItem.toModel(): LoginModel.LoginItemModel {
        return LoginModel.LoginItemModel(
            this.userId,
            this.name,
            this.token
        )
    }

    fun LoginResponse.toModel(): LoginModel {
        return LoginModel(
            this.error,
            this.message,
            this.data?.toModel()
        )
    }
}