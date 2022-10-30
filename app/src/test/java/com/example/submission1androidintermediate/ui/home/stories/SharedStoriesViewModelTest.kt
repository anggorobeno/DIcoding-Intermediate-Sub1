package com.example.submission1androidintermediate.ui.home.stories

import com.example.domain.model.stories.ImageModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.CoroutinesTest
import com.example.submission1androidintermediate.helper.getOrAwaitValue
import com.example.submission1androidintermediate.usecase.stories.ErrorStoriesUseCase
import com.example.submission1androidintermediate.usecase.stories.SuccessStoriesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotBeTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class SharedStoriesViewModelTest : CoroutinesTest() {
    private lateinit var storiesViewModel: SharedStoriesViewModel
    private lateinit var successStoriesUseCase: StoriesUseCase
    private lateinit var errorUseCase: StoriesUseCase
    private val desc = "tes"
    private val latitude = "142.2"
    private val longitude = "1235.5"
    private val file = File("dir/picture")

    @Before
    fun setUp() {
        errorUseCase = ErrorStoriesUseCase()
        successStoriesUseCase = SuccessStoriesUseCase()
    }

    @Test
    fun `when calling uploadImage should not null and return success`() {
        runTest {
            storiesViewModel = SharedStoriesViewModel(successStoriesUseCase)
            storiesViewModel.ioDispatcher = testDispatcher
            storiesViewModel.mainDispatcher = testDispatcher
            storiesViewModel.uploadImage(desc, file, latitude, longitude)
            val actualData = storiesViewModel.storiesUploadResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeInstanceOf NetworkResult.Success::class
            actualData.data?.message shouldBeEqualTo "No Error"
            actualData.data?.error?.shouldNotBeTrue()
        }
    }

    @Test
    fun `when calling uploadImage with network error should return result error`() {
        runTest {
            storiesViewModel = SharedStoriesViewModel(errorUseCase)
            storiesViewModel.ioDispatcher = testDispatcher
            storiesViewModel.mainDispatcher = testDispatcher
            storiesViewModel.uploadImage(desc, file, latitude, longitude)
            val actualData = storiesViewModel.storiesUploadResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeInstanceOf NetworkResult.Error::class
            actualData.message?.peekContent() shouldBeEqualTo "network is error"
        }
    }

    @Test
    fun `when calling saveImageResult should not null and return correct data`() {
        runTest {
            storiesViewModel = SharedStoriesViewModel(successStoriesUseCase)
            storiesViewModel.ioDispatcher = testDispatcher
            storiesViewModel.mainDispatcher = testDispatcher
            val file = File("dir/picture")
            storiesViewModel.saveImageResult(file, true)
            val actualData = storiesViewModel.imageBitmap.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeInstanceOf ImageModel::class
            actualData shouldBeEqualTo ImageModel(file, true)
        }
    }
}