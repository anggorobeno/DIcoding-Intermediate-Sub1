package com.example.submission1androidintermediate.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.login.LoginModel
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: UserUseCase) : ViewModel() {
    private var _loginResult = MutableLiveData<NetworkResult<LoginModel>>()
    val loginResult : LiveData<NetworkResult<LoginModel>> = _loginResult
    val isEmailError = MutableLiveData<Boolean>(false)
    val isPasswordError = MutableLiveData<Boolean>(false)
    fun doLoginUser(body: LoginRequest) {
        viewModelScope.launch {
            useCase.loginUser(body)
                .collectLatest {
                    _loginResult.value = it
                }
        }
    }

    fun setButtonError(isErrorEmail: Boolean, isErrorPassword: Boolean) {
        viewModelScope.launch {
            this@LoginViewModel.isEmailError.value = isErrorEmail
            this@LoginViewModel.isPasswordError.value = isErrorPassword
        }
    }
}