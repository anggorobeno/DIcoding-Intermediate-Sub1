package com.example.submission1androidintermediate.ui.home

import androidx.paging.AsyncPagingDataDiffer
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.submission1androidintermediate.helper.*
import com.example.submission1androidintermediate.usecase.stories.SuccessStoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : CoroutinesTest() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var successStoriesUseCase: StoriesUseCase

    @Before
    fun setUp() {
        successStoriesUseCase = SuccessStoriesUseCase()
    }

    @Test
    fun `when calling getStoriesPaging should return correct mapped data and not null`() {
        coTest {
            homeViewModel = HomeViewModel(successStoriesUseCase)
            val actualData = homeViewModel.storiesPagingResult.getOrAwaitValue()
            val expectedData = TestHelper.provideStoriesModelItem()
            actualData.shouldNotBeNull()
            val differ = AsyncPagingDataDiffer(
                diffCallback = MyDiffCallback(),
                updateCallback = NoopListCallback(),
                workerDispatcher = Dispatchers.Main
            )
            differ.submitData(actualData)
            differ.snapshot().items shouldBeEqualTo expectedData
        }
    }
}