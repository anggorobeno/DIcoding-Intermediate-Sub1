package com.example.submission1androidintermediate.ui.home.stories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.camera.core.internal.utils.ImageUtil
import androidx.core.graphics.createBitmap
import com.example.domain.model.stories.ImageModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.CoroutinesTest
import com.example.submission1androidintermediate.helper.ImageUtils
import com.example.submission1androidintermediate.helper.getOrAwaitValue
import com.example.submission1androidintermediate.usecase.stories.ErrorStoriesUseCase
import com.example.submission1androidintermediate.usecase.stories.SuccessStoriesUseCase
import com.google.common.truth.Truth
import dalvik.annotation.TestTarget
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.File

@ExperimentalCoroutinesApi
class SharedStoriesViewModelTest : CoroutinesTest() {
    private lateinit var storiesViewModel: SharedStoriesViewModel
    private lateinit var successStoriesUseCase: StoriesUseCase
    private lateinit var errorUseCase: StoriesUseCase
    private lateinit var imageUtil: ImageUtils



    @Before
    fun setUp() {
        errorUseCase = ErrorStoriesUseCase()
        successStoriesUseCase = SuccessStoriesUseCase()
    }

    @Test
    fun `when calling uploadImage should not null and return success`() {
        coTest {
            storiesViewModel = SharedStoriesViewModel(successStoriesUseCase)
            storiesViewModel.ioDispatcher = testDispatcher
            storiesViewModel.mainDispatcher = testDispatcher
            val desc = "tes"
            val latitude = "142.2"
            val longitude = "1235.5"
            val file = File("dir/picture")
            storiesViewModel.uploadImage(desc, file, latitude, longitude)
            val actualData = storiesViewModel.storiesUploadResult.getOrAwaitValue()
            Truth.assertThat(actualData).isNotNull()
            actualData shouldBeInstanceOf NetworkResult.Success::class
        }
    }

    @Test
    fun `when calling saveImageResult should not null and return correct data`() {
        coTest {
            storiesViewModel = SharedStoriesViewModel(successStoriesUseCase)
            storiesViewModel.ioDispatcher = testDispatcher
            storiesViewModel.mainDispatcher = testDispatcher
            val file = File("dir/picture")
            storiesViewModel.saveImageResult(file, true)
            val actualData = storiesViewModel.imageBitmap.getOrAwaitValue()
            Truth.assertThat(actualData).isNotNull()
            actualData shouldBeInstanceOf ImageModel::class
            actualData shouldBeEqualTo ImageModel(file,true)
        }
    }
}