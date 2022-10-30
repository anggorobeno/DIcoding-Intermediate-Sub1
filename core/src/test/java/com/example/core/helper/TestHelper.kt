package com.example.core.helper

import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.response.story.StoriesUploadResponse
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.register.RegisterRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

object TestHelper {
    private fun provideLoginResponseItem() = LoginResponse.LoginItem("123", "Anggoro", "123")

    fun provideLoginResponse() = LoginResponse(provideLoginResponseItem())

    fun provideErrorResponse(): ResponseBody {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        return errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
    }

    fun provideLoginRequest() = LoginRequest("anggoro131098@gmail.com", "testaja")

    fun provideRegisterResponse() = RegisterResponse()

    fun provideRegisterRequest() = RegisterRequest(
        "Anggoro",
        "anggoro@gmail.com",
        "testaja"
    )

    private fun provideStoriesResponseItem(): ArrayList<StoriesResponse.StoriesResponseItem> {
        return arrayListOf(
            StoriesResponse.StoriesResponseItem(
                "123",
                "Anggoro",
                "tes",
                "13123",
                "sdfsd",
                "123",
                "123"
            )
        )
    }

    fun provideStoriesResponse() = StoriesResponse(
        provideStoriesResponseItem()
    )


    fun provideStoriesUploadResponse() = StoriesUploadResponse()

    private fun provideStoriesModelItem() = StoriesModel.StoriesModelItem(
        "123", "a", "tes", "fsdf", "2", "2", "2"
    )
}
