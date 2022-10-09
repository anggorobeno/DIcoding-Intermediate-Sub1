package com.example.domain.usecase.stories

import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.repository.stories.IStoriesRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesInteractor(private val repository: IStoriesRepository) : StoriesUseCase {
    override suspend fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return repository.getStories()
    }

    override suspend fun uploadStories(
        description: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>> {
        return repository.uploadStories(description, file)
    }
}