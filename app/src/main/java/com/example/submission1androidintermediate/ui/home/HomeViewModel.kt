package com.example.submission1androidintermediate.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.stories.StoriesModel
import com.example.domain.model.stories.StoriesUploadModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.helper.AppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: StoriesUseCase) : ViewModel() {
    private var _storiesResult = MutableLiveData<NetworkResult<StoriesModel>>()
    val storiesResult: LiveData<NetworkResult<StoriesModel>> = _storiesResult
    private var _successToastText = MutableLiveData<SingleEvent<String?>>()
    val toastText: LiveData<SingleEvent<String?>> = _successToastText
    init {
        getStories()
    }

    fun getStories() {
        viewModelScope.launch {
            useCase.getStories().collectLatest {
                _storiesResult.value = it
                _successToastText.value = SingleEvent(it.data?.message)
            }
        }
    }

}