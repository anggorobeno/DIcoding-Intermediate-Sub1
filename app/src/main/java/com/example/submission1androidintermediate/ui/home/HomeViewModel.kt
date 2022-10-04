package com.example.submission1androidintermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: StoriesUseCase) : ViewModel() {
    private var _storiesResult = MutableLiveData<NetworkResult<StoriesModel>>()
    val storiesResult: LiveData<NetworkResult<StoriesModel>> = _storiesResult

    fun getStories() {
        viewModelScope.launch {
            useCase.getStories().collectLatest {
                _storiesResult.value = it
            }
        }
    }

}