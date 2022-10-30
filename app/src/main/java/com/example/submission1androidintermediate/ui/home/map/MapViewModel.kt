package com.example.submission1androidintermediate.ui.home.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.core.data.utils.Mapper.toMapModel
import com.example.domain.model.stories.MapModel
import com.example.domain.model.stories.StoriesModel
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val storiesUseCase: StoriesUseCase) : ViewModel() {
    fun getStoriesLocation(): LiveData<NetworkResult<StoriesModel>> {
        return liveData {
            storiesUseCase.getStories().collectLatest {
                emit(it)
            }
        }
    }

    fun getMapModel(listStories: List<StoriesModel.StoriesModelItem>): List<MapModel> {
        return listStories.map {
            it.toMapModel()
        }
    }

}