package com.example.core.data.source.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import app.cash.turbine.test
import com.example.core.helper.TestHelper
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.amshove.kluent.should
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.notNull
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.File
import javax.inject.Provider

@ExperimentalCoroutinesApi
class StoriesRepositoryTest {
    //SUT
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var storiesDataSource: StoriesDataSource
    private lateinit var storiesPagingSource: FakeStoriesPagingSource
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        storiesPagingSource = FakeStoriesPagingSource()
        storiesDataSource = mock()
        storiesRepository = StoriesRepository(storiesDataSource) {
            storiesPagingSource
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when calling getStories should emit loading and success`() {
        testScope.runTest {
            whenever(storiesDataSource.getStories()).thenReturn(
                Response.success(TestHelper.provideStoriesResponse())
            )
            val actualData = storiesRepository.getStories()
            actualData.test {
                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Success::class

                awaitComplete()
            }
        }
    }

    @Test
    fun `when calling getStories with network error should emit loading and error`() {
        testScope.runTest {
            whenever(storiesDataSource.getStories()).thenReturn(
                Response.error(500, TestHelper.provideErrorResponse())
            )
            val actualData = storiesRepository.getStories()
            actualData.test {
                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Error::class

                awaitComplete()
            }
        }
    }

    @Test
    fun `when calling uploadStories should emit loading and success`() {
        runTest {
            val desc = "tes".toRequestBody("text/plain".toMediaType())
            val latitude = "142.2".toRequestBody("text/plain".toMediaType())
            val longitude = "1235.5".toRequestBody("text/plain".toMediaType())
            val file = File("dir/picture").asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                "test upload image",
                file
            )
            whenever(
                storiesDataSource.uploadStories(
                    desc,
                    latitude,
                    longitude,
                    imageMultipart
                )
            ).thenReturn(Response.success(TestHelper.provideStoriesUploadResponse()))
            val actualData = storiesRepository.uploadStories(
                desc,
                latitude,
                longitude,
                imageMultipart
            )
            actualData.test {
                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Success::class

                awaitComplete()
            }
        }
    }

    @Test
    fun `when calling uploadStories should emit loading and error`() {
        testScope.runTest {
            val desc = "tes".toRequestBody("text/plain".toMediaType())
            val latitude = "142.2".toRequestBody("text/plain".toMediaType())
            val longitude = "1235.5".toRequestBody("text/plain".toMediaType())
            val file = File("dir/picture").asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                "test upload image",
                file
            )
            whenever(
                storiesDataSource.uploadStories(
                    desc,
                    latitude,
                    longitude,
                    imageMultipart
                )
            ).thenReturn(Response.error(500, TestHelper.provideErrorResponse()))
            val actualData = storiesRepository.uploadStories(
                desc,
                latitude,
                longitude,
                imageMultipart
            )
            actualData.test {
                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Error::class

                awaitComplete()
            }
        }
    }

    @Test
    fun `when calling getStoriesPaging should not null`() {
        testScope.runTest {
            val actualData = storiesRepository.getStoriesPaging()
            actualData.test {
                val pagingData = awaitItem()
                Truth.assertThat(pagingData).isNotNull()
            }
        }
    }
}