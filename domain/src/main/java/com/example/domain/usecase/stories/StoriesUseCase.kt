package com.example.domain.usecase.stories

import androidx.paging.PagingData
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoriesUseCase {
    fun getStories(): Flow<NetworkResult<StoriesModel>>
    fun uploadStories(
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>>

    fun getStoriesPaging(): Flow<PagingData<StoriesModel.StoriesModelItem>>
}