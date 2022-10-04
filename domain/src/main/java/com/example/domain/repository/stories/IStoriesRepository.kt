package com.example.domain.repository.stories

import com.example.domain.model.stories.StoriesModel
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IStoriesRepository {
    suspend fun getStories(): Flow<NetworkResult<StoriesModel>>
}