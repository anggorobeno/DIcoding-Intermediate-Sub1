package com.example.submission1androidintermediate.helper

import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.submission1androidintermediate.ui.login.LoginViewModel

object TestHelper {
    // RegisterViewModel
    fun provideSuccessRegisterModel(): RegisterModel {
        return RegisterModel(
            false,
            "No Error",
        )
    }

    fun provideErrorRegisterModel() = RegisterModel(true, "Network is error")

    fun provideRegisterRequest() = RegisterRequest(
        "Anggoro",
        "anggoro@gmail.com",
        "testaja"
    )

    // LoginViewModel
    fun provideSuccessLoginModel() = LoginModel(
        false,
        "No Error",
        LoginModel.LoginItemModel(
            "asd123",
            "Anggoro",
            "123"
        )
    )

    fun provideLoginRequest() = LoginRequest("anggoro131098@gmail.com", "testaja")


    // HomeViewModel
    fun provideStoriesModelItem() = arrayListOf<StoriesModel.StoriesModelItem>(
        StoriesModel.StoriesModelItem(
            "1", "Anggoro", "Ini test aja", "blabla", "kapankapan", "123z",
            "222d"
        )
    )

    fun provideSuccessStoriesModel() = StoriesModel(
        provideStoriesModelItem(), "No Error", false
    )

    fun provideUploadStoriesSuccessModel() = StoriesUploadModel(
        "No Error", false
    )


}