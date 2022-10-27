package com.example.submission1androidintermediate.ui.home

import androidx.paging.PagingData
import androidx.paging.map
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.submission1androidintermediate.helper.CoroutinesTest
import com.example.submission1androidintermediate.helper.TestHelper
import com.example.submission1androidintermediate.helper.getOrAwaitValue
import com.example.submission1androidintermediate.usecase.stories.SuccessStoriesUseCase
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
@ExperimentalCoroutinesApi
class HomeViewModelTest : CoroutinesTest() {
    private lateinit var successHomeViewModel: HomeViewModel
    private lateinit var errorHomeViewModel: HomeViewModel
    private lateinit var successStoriesUseCase: StoriesUseCase

    @Before
    fun setUp() {
        successStoriesUseCase = SuccessStoriesUseCase()
        successHomeViewModel = HomeViewModel(successStoriesUseCase)
    }

    @Test
    fun `when calling getStoriesPaging should return correct data and not null`() {
        coTest {
            val actualData = successHomeViewModel.storiesPagingResult.getOrAwaitValue()
            val expectedData = TestHelper.provideStoriesModelItem()
            Truth.assertThat(actualData).isNotNull()
            actualData.map {
                Truth.assertThat(it).isEqualTo(expectedData)
            }
        }

    }
}