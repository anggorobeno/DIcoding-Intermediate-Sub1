package com.example.core.data.source.stories

import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.services.DicodingStoryApiService
import retrofit2.Response

class StoriesDataSourceImpl(private val apiService: DicodingStoryApiService) : StoriesDataSource {
    override suspend fun getStories(): Response<StoriesResponse> {
        return apiService.getStories()
    }
}