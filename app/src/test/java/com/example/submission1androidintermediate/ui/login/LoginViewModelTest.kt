package com.example.submission1androidintermediate.ui.login

import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.helper.CoroutinesTest
import com.example.submission1androidintermediate.helper.FakePreferenceDataStoreHelper
import com.example.submission1androidintermediate.helper.TestHelper
import com.example.submission1androidintermediate.helper.getOrAwaitValue
import com.example.submission1androidintermediate.usecase.user.ErrorUserUseCase
import com.example.submission1androidintermediate.usecase.user.SuccessUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotBeTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest : CoroutinesTest() {
    private lateinit var successUseCase: UserUseCase
    private lateinit var errorUseCase: UserUseCase
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dataStoreImpl: FakePreferenceDataStoreHelper

    @Before
    fun setUp() {
        dataStoreImpl = FakePreferenceDataStoreHelper()
        successUseCase = SuccessUserUseCase()
        errorUseCase = ErrorUserUseCase()
    }

    @Test
    fun `when calling doLoginUser should return Success`() {
        coTest {
            loginViewModel = LoginViewModel(successUseCase)
            loginViewModel.ioDispatcher = testDispatcher
            loginViewModel.doLoginUser(TestHelper.provideLoginRequest())
            val actualData = loginViewModel.loginResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeInstanceOf NetworkResult.Success::class
            actualData.data?.message shouldBeEqualTo "No Error"
            actualData.data?.error?.shouldNotBeTrue()
        }
    }

    @Test
    fun `when calling doLoginUser with network error should return Network Result error `() {
        coTest {
            loginViewModel = LoginViewModel(errorUseCase)
            loginViewModel.ioDispatcher = testDispatcher
            loginViewModel.doLoginUser(TestHelper.provideLoginRequest())
            val actualData = loginViewModel.loginResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeEqualTo NetworkResult.Error::class
            actualData.message shouldBeEqualTo SingleEvent("network is error")
        }
    }

    @Test
    fun `when calling saveUserToken should save token to data store`() {
        coTest {
            loginViewModel = LoginViewModel(successUseCase)
            loginViewModel.prefs = dataStoreImpl
            loginViewModel.ioDispatcher = testDispatcher
            loginViewModel.saveUserToken("123")
            loginViewModel.prefs.getLoginStatus() shouldBeEqualTo 123
        }
    }

    @Test
    fun `when calling setLoginStatus should get save login status to data store`() {
        coTest {
            loginViewModel = LoginViewModel(successUseCase)
            loginViewModel.prefs = dataStoreImpl
            loginViewModel.ioDispatcher = testDispatcher
            loginViewModel.setLoginStatus(true)
            loginViewModel.prefs.getLoginStatus() shouldBeEqualTo true
        }
    }
}