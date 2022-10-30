package com.example.core.data.source.stories

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.remote.response.story.StoriesResponse
import com.example.core.helper.TestHelper
import com.example.domain.model.stories.StoriesModel

class FakeStoriesPagingSource : PagingSource<Int, StoriesResponse.StoriesResponseItem>() {
    companion object {
        fun snapshot(items: List<StoriesResponse.StoriesResponseItem>): PagingData<StoriesResponse.StoriesResponseItem> {
            return PagingData.from(items)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true

    override fun getRefreshKey(state: PagingState<Int, StoriesResponse.StoriesResponseItem>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResponse.StoriesResponseItem> {
        return LoadResult.Page(emptyList(), null, 1)
    }
}