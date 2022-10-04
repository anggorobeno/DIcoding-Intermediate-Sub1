package com.example.core.data.source.stories

import com.example.core.data.utils.BaseApiCall
import com.example.core.data.utils.Mapper.toModel
import com.example.domain.model.stories.StoriesModel
import com.example.domain.repository.stories.IStoriesRepository
import com.example.domain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StoriesRepository(private val remoteDataSource: StoriesDataSource) : IStoriesRepository {
    override suspend fun getStories(): Flow<NetworkResult<StoriesModel>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(BaseApiCall.safeApiCall({
                remoteDataSource.getStories()
            }, { response ->
                response!!.toModel()
            }))
        }
    }
}