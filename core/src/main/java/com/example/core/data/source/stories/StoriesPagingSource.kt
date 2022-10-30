package com.example.core.data.source.stories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.data.remote.services.DicodingStoryApiService
import com.github.ajalt.timberkt.Timber

class StoriesPagingSource(private val apiService: DicodingStoryApiService) :
    PagingSource<Int, StoriesResponse.StoriesResponseItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoriesResponse.StoriesResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResponse.StoriesResponseItem> {
        try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)
            return LoadResult.Page(
                data = responseData.body()?.data ?: emptyList(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.body()?.data.isNullOrEmpty()) null else position + 1
            )
//            else {
//                val errorResponse: GenericStatusResponse = Gson().fromJson(
//                    responseData.errorBody()?.charStream(),
//                    GenericStatusResponse()::class.java
//                )
//                errorResponse.message?.let {
//                    Timber.d {
//                        "ApiCall failed: $it"
//                    }
//                    return LoadResult
//                }
//            }
        } catch (exception: Exception) {
            Timber.d(exception)
            return LoadResult.Error(exception)
        }
    }
}