package com.example.submission1androidintermediate.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.helper.MainCoroutineRule
import com.example.submission1androidintermediate.helper.TestHelper
import com.example.submission1androidintermediate.helper.getOrAwaitValue
import com.example.submission1androidintermediate.ui.home.adapter.HomeStoryPagingAdapter
import com.example.submission1androidintermediate.usecase.ErrorUserUseCase
import com.example.submission1androidintermediate.usecase.SuccessUserUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule


import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var successUseCase: UserUseCase
    private lateinit var errorUseCase: UserUseCase
    private lateinit var successRegisterViewModel: RegisterViewModel
    private lateinit var errorRegisterViewModel: RegisterViewModel

    @Before
    fun setUp() {
        successUseCase = SuccessUserUseCase()
        errorUseCase = ErrorUserUseCase()
        successRegisterViewModel = RegisterViewModel(successUseCase)
        errorRegisterViewModel = RegisterViewModel(errorUseCase)
        successRegisterViewModel.doRegisterUser(
            TestHelper.provideRegisterRequest()
        )
        errorRegisterViewModel.doRegisterUser(
            TestHelper.provideRegisterRequest()
        )
    }

    @Test
    fun `when calling doRegisterUser should return Result Success and not null`() {
        runTest {
            val actualData = successRegisterViewModel.registerResult.getOrAwaitValue()
            assertThat(actualData).isNotNull()
            assertThat(actualData.data?.message).isEqualTo("No Error")
            assertThat(actualData.data?.error).isFalse()
            assertThat(actualData is NetworkResult.Success).isTrue()
            assertThat((actualData as NetworkResult.Success).data?.message).isEqualTo("No Error")
        }
    }

    @Test
    fun `when Network Error should return Result error`() {
        runTest {
            val actualData = errorRegisterViewModel.registerResult.getOrAwaitValue()
            assertThat(actualData).isNotNull()
            assertThat(actualData is NetworkResult.Error).isTrue()
            assertThat((actualData as NetworkResult.Error).message).isInstanceOf(SingleEvent("network is error")::class.java)
        }
    }
}