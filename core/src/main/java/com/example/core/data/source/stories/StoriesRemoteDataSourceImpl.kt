package com.example.core.data.source.stories

import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.response.story.StoriesUploadResponse
import com.example.core.data.remote.services.DicodingStoryApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class StoriesRemoteDataSourceImpl(private val apiService: DicodingStoryApiService) : StoriesDataSource {
    override suspend fun getStories(): Response<StoriesResponse> {
        return apiService.getStories()
    }

    override suspend fun uploadStories(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<StoriesUploadResponse> {
        return apiService.uploadStories(file, description)
    }
}