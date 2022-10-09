package com.example.domain.usecase.stories

import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoriesUseCase {
    suspend fun getStories(): Flow<NetworkResult<StoriesModel>>
    suspend fun uploadStories(
        description: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>>
}