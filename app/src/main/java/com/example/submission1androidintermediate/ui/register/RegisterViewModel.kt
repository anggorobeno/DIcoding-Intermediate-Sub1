package com.example.submission1androidintermediate.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.user.register.RegisterModel
import com.example.domain.model.user.register.RegisterRequest
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val useCase: UserUseCase) : ViewModel() {
    private var _registerResult = MutableLiveData<NetworkResult<RegisterModel>>()
    val registerResult: LiveData<NetworkResult<RegisterModel>> = _registerResult

    fun doRegisterUser(body: RegisterRequest) {
        viewModelScope.launch {
            useCase.registerUser(body).collectLatest {
                _registerResult.value = it
            }
        }
    }

}