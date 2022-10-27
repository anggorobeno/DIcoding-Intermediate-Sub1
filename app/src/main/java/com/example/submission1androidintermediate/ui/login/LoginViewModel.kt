package com.example.submission1androidintermediate.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.local.IDataStore
import com.example.core.di.CoroutinesQualifier
import com.example.domain.model.user.login.LoginModel
import com.example.domain.model.user.login.LoginRequest
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: UserUseCase) : ViewModel() {
    private var _loginResult = MutableLiveData<NetworkResult<LoginModel>>()

    @Inject
    @CoroutinesQualifier.IoDispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var prefs: IDataStore
    val loginResult: LiveData<NetworkResult<LoginModel>> = _loginResult
    fun doLoginUser(body: LoginRequest): Job {
        return viewModelScope.launch {
            useCase.loginUser(body)
                .collectLatest {
                    _loginResult.value = it
                }
        }
    }

    fun saveUserToken(token: String): Job {
        val job = viewModelScope.launch(ioDispatcher) {
            prefs.saveUserToken(token)
            Timber.d(prefs.getUserToken())
        }
        return job
    }

    fun setLoginStatus(isLogin: Boolean): Job {
        val job = viewModelScope.launch(ioDispatcher) {
            prefs.setLoginStatus(isLogin)
            Timber.d(prefs.getLoginStatus().toString())
        }
        return job
    }
}