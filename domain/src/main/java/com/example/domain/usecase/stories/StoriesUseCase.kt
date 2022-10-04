package com.example.domain.usecase.stories

import com.example.domain.model.stories.StoriesModel
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StoriesUseCase {
    suspend fun getStories(): Flow<NetworkResult<StoriesModel>>
}