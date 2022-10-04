package com.example.domain.usecase.stories

import com.example.domain.model.stories.StoriesModel
import com.example.domain.repository.stories.IStoriesRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class StoriesInteractor(private val repository: IStoriesRepository) : StoriesUseCase {
    override suspend fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return repository.getStories()
    }
}