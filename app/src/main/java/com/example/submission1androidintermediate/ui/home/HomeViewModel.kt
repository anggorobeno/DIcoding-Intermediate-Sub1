package com.example.submission1androidintermediate.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.domain.model.stories.MapModel
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: StoriesUseCase) : ViewModel() {
    private var _storiesResult = MutableLiveData<NetworkResult<StoriesModel>>()
    val storiesResult: LiveData<NetworkResult<StoriesModel>> = _storiesResult
    private var _storiesPagingResult = MutableLiveData<PagingData<StoriesModel.StoriesModelItem>>()
    val storiesPagingResult: LiveData<PagingData<StoriesModel.StoriesModelItem>> get() = _storiesPagingResult
    private var _successToastText = MutableLiveData<SingleEvent<String?>>()
    val toastText: LiveData<SingleEvent<String?>> = _successToastText


    init {
        getStoriesPaging()
    }

    fun getStoriesPaging() {
        viewModelScope.launch {
            useCase.getStoriesPaging().cachedIn(viewModelScope).collectLatest {
                _storiesPagingResult.value = it
            }
        }
    }

}