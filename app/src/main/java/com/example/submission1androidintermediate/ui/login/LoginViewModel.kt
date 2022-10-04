package com.example.submission1androidintermediate.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.local.PreferencesDataStore
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.model.user.login.LoginModel
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: UserUseCase) : ViewModel() {
    private var _loginResult = MutableLiveData<NetworkResult<LoginModel>>()

    @Inject
    lateinit var prefs: PreferencesDataStore
    val loginResult: LiveData<NetworkResult<LoginModel>> = _loginResult
    val isEmailError = MutableLiveData<Boolean>(false)
    val isPasswordError = MutableLiveData<Boolean>(false)
    fun doLoginUser(body: LoginRequest): Job {
        return viewModelScope.launch {
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

    fun saveUserToken(token: String): Job {
        val job = viewModelScope.launch {
            prefs.saveUserToken(token)
            Timber.d(prefs.getUserToken())
        }
        return job
    }

    fun setLoginStatus(isLogin: Boolean): Job {
        val job = viewModelScope.launch {
            prefs.setLoginStatus(isLogin)
            Timber.d(prefs.getLoginStatus().toString())
        }
        return job
    }
}