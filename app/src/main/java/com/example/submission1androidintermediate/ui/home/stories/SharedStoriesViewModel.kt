package com.example.submission1androidintermediate.ui.home.stories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.CoroutinesQualifier
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedStoriesViewModel @Inject constructor(private val useCase: StoriesUseCase) :
    ViewModel() {
    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher
    @Inject
    @CoroutinesQualifier.MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher
    private var _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap> get() = _imageBitmap

    private var _storiesUploadResult = MutableLiveData<NetworkResult<StoriesUploadModel>>()
    val storiesUploadResult: LiveData<NetworkResult<StoriesUploadModel>> get() = _storiesUploadResult

    fun uploadImage(description: String, file: File) {
        viewModelScope.launch(ioDispatcher) {
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile =
                ImageUtils.reduceFileImage(file).asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            withContext(mainDispatcher){
                useCase.uploadStories(desc, imageMultipart).collectLatest {
                    _storiesUploadResult.value = it
                }
            }

        }

    }

    fun saveImageResult(image: File, isBackCamera: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            val bitmap = BitmapFactory.decodeFile(image.path)
            val result = ImageUtils.rotateBitmap(bitmap,isBackCamera)
            withContext(mainDispatcher){
                _imageBitmap.value = result
            }
        }
    }
}