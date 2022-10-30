package com.example.submission1androidintermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: StoriesUseCase) : ViewModel() {
    private var _storiesPagingResult = MutableLiveData<PagingData<StoriesModel.StoriesModelItem>>()
    val storiesPagingResult: LiveData<PagingData<StoriesModel.StoriesModelItem>> get() = _storiesPagingResult


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