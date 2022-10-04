package com.example.core.data.source.stories

import com.example.core.data.remote.response.story.StoriesResponse
import retrofit2.Response

interface StoriesDataSource {
    suspend fun getStories(): Response<StoriesResponse>
}