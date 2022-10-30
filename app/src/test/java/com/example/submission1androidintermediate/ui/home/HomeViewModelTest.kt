package com.example.submission1androidintermediate.ui.home

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.submission1androidintermediate.helper.*
import com.example.submission1androidintermediate.usecase.stories.ErrorStoriesUseCase
import com.example.submission1androidintermediate.usecase.stories.SuccessStoriesUseCase
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : CoroutinesTest() {
    private lateinit var successHomeViewModel: HomeViewModel
    private lateinit var errorHomeViewModel: HomeViewModel
    private lateinit var successStoriesUseCase: StoriesUseCase
    private lateinit var errorUseCase: StoriesUseCase

    @Before
    fun setUp() {
        successStoriesUseCase = SuccessStoriesUseCase()
        successHomeViewModel = HomeViewModel(successStoriesUseCase)
        errorUseCase = ErrorStoriesUseCase()
        errorHomeViewModel = HomeViewModel(errorUseCase)
    }

    @Test
    fun `when calling getStoriesPaging should return correct mapped data and not null`() {
        runTest {
            val actualData = successHomeViewModel.storiesPagingResult.getOrAwaitValue()
            val expectedData = TestHelper.provideStoriesModelItem()
            assertThat(actualData).isNotNull()
            val differ = AsyncPagingDataDiffer(
                diffCallback = MyDiffCallback(),
                updateCallback = NoopListCallback(),
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(actualData)
            differ.snapshot().items shouldBeEqualTo expectedData
        }
    }

    @Test
    fun `when calling getStoriesPaging paging with network error should return error`() {
        coTest {
            val actualData = errorHomeViewModel.storiesPagingResult.getOrAwaitValue()
        }
    }
}