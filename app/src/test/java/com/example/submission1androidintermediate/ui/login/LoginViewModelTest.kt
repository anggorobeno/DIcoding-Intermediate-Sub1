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
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest : CoroutinesTest() {
    private lateinit var successUseCase: UserUseCase
    private lateinit var errorUseCase: UserUseCase
    private lateinit var successLoginViewModel: LoginViewModel
    private lateinit var errorLoginViewModel: LoginViewModel
    private lateinit var preferencesScope: CoroutineScope
    private lateinit var dataStoreImpl: FakePreferenceDataStoreHelper

    @Before
    fun setUp() {
        preferencesScope = CoroutineScope(testDispatcher + Job())
        dataStoreImpl = FakePreferenceDataStoreHelper()
        successUseCase = SuccessUserUseCase()
        errorUseCase = ErrorUserUseCase()
        successLoginViewModel = LoginViewModel(successUseCase)
        errorLoginViewModel = LoginViewModel(errorUseCase)
        successLoginViewModel.prefs = dataStoreImpl
        successLoginViewModel.ioDispatcher = testDispatcher
    }

    @After
    fun tearDown() {
        preferencesScope.cancel()
    }

    @Test
    fun `when calling doLoginUser should return Success`() {
        coTest {
            successLoginViewModel.doLoginUser(TestHelper.provideLoginRequest())
            val actualData = successLoginViewModel.loginResult.getOrAwaitValue()
            assertThat(actualData).isNotNull()
            Truth.assertThat(actualData.data?.message).isEqualTo("No Error")
            Truth.assertThat(actualData.data?.error).isFalse()
            Truth.assertThat(actualData is NetworkResult.Success).isTrue()
            Truth.assertThat((actualData as NetworkResult.Success).data?.message)
                .isEqualTo("No Error")
        }
    }

    @Test
    fun `when network error should return Network Result error `() {
        coTest {
            errorLoginViewModel.doLoginUser(TestHelper.provideLoginRequest())
            val actualData = errorLoginViewModel.loginResult.getOrAwaitValue()
            assertThat(actualData).isNotNull()
            assertThat(actualData is NetworkResult.Error).isTrue()
            assertThat((actualData as NetworkResult.Error).message)
                .isInstanceOf(SingleEvent("network is error")::class.java)
        }
    }

    @Test
    fun `when calling saveUserToken should save token to data store`() {
        coTest {
            successLoginViewModel.saveUserToken("123")
            Truth.assertThat(successLoginViewModel.prefs.getUserToken()).isEqualTo("123")
        }
    }

    @Test
    fun `when calling setLoginStatus should get save login status to data store`(){
        coTest {
            successLoginViewModel.setLoginStatus(true)
            Truth.assertThat(successLoginViewModel.prefs.getLoginStatus()).isTrue()
        }
    }
}