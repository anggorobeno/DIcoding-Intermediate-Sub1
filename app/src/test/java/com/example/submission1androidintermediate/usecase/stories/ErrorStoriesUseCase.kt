package com.example.submission1androidintermediate.usecase.stories

import androidx.paging.PagingData
import androidx.paging.PagingState
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ErrorStoriesUseCase : StoriesUseCase {
    override fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return flow {
            emit(NetworkResult.Error(SingleEvent("network is error")))
        }
    }

    override fun uploadStories(
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>> {
        return flow {
            emit(NetworkResult.Error(SingleEvent("network is error")))
        }
    }

    override fun getStoriesPaging(): Flow<PagingData<StoriesModel.StoriesModelItem>> {
        return flow {

        }
    }
}