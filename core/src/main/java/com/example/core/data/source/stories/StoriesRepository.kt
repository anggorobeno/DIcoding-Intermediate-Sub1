package com.example.core.data.source.stories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.data.utils.BaseApiCall
import com.example.core.data.utils.Mapper.toModel
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.repository.stories.IStoriesRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Provider

class StoriesRepository(
    private val remoteDataSource: StoriesDataSource,
    private val storiesPagingSource: Provider<StoriesPagingSource>
) : IStoriesRepository {
    override fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                remoteDataSource.getStories()
            }, { response ->
                response!!.toModel()
            }))
        }.flowOn(Dispatchers.IO)
    }

    override fun uploadStories(
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<StoriesUploadModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                remoteDataSource.uploadStories(description, lat, lon, file)
            }, { response ->
                response!!.toModel()
            }))
        }.flowOn(Dispatchers.IO)
    }

    override fun getStoriesPaging(): Flow<PagingData<StoriesModel.StoriesModelItem>> {
        return Pager(
            config = PagingConfig(
                4,
            ),
            pagingSourceFactory = {
                storiesPagingSource.get()
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }
}