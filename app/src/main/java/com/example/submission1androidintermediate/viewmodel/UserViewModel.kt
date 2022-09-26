package com.example.submission1androidintermediate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.networkutils.NetworkResult
import com.example.core.data.remote.request.LoginRequest
import com.example.domain.model.user.LoginModel
import com.example.domain.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel@Inject constructor(private val useCase: UserUseCase): ViewModel() {
     val result = MutableLiveData<NetworkResult<LoginModel>>()
    fun loginUser(body: LoginRequest){
        viewModelScope.launch {
            useCase.loginUser(body)
                .collectLatest {
                    result.value = it
                }
        }
    }
}