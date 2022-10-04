package com.example.core.data.remote.services

import com.example.core.data.remote.response.story.FileUploadResponse
import com.example.domain.model.user.login.LoginRequest
import com.example.core.data.remote.response.user.login.LoginResponse
import com.example.core.data.remote.response.user.register.RegisterResponse
import com.example.domain.model.user.register.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DicodingStoryApiService {
    @POST("login")
    suspend fun loginUser(@Body requestBody: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body requestBody: RegisterRequest): Response<RegisterResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<FileUploadResponse>
}