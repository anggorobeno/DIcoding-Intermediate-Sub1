package com.example.core.data.source.stories

import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.response.story.StoriesUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface StoriesDataSource {
    suspend fun getStories(): Response<StoriesResponse>
    suspend fun uploadStories(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<StoriesUploadResponse>
}