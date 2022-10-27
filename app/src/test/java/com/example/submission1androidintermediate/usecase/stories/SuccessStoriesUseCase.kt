package com.example.submission1androidintermediate.usecase.stories

import androidx.paging.PagingData
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.TestHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SuccessStoriesUseCase : StoriesUseCase {
    override fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return flow {
            emit(
                NetworkResult.Success(
                    TestHelper.provideSuccessStoriesModel()
                )
            )
        }
    }

    override fun uploadStories(
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>> {
        return flow {
            emit(
                NetworkResult.Success(
                    TestHelper.provideUploadStoriesSuccessModel()
                )
            )
        }
    }

    override fun getStoriesPaging(): Flow<PagingData<StoriesModel.StoriesModelItem>> {
        return flow {
            emit(
                PagingData.from(TestHelper.provideStoriesModelItem())
            )
        }
    }
}