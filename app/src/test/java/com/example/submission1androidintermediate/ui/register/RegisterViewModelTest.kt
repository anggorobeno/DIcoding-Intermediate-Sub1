package com.example.submission1androidintermediate.ui.register


import com.example.domain.usecase.user.UserUseCase
import com.example.domain.utils.NetworkResult
import com.example.domain.utils.SingleEvent
import com.example.submission1androidintermediate.helper.CoroutinesTest
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
class RegisterViewModelTest : CoroutinesTest() {
    private lateinit var successUseCase: UserUseCase
    private lateinit var errorUseCase: UserUseCase
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        successUseCase = SuccessUserUseCase()
        errorUseCase = ErrorUserUseCase()
    }

    @Test
    fun `when calling doRegisterUser should return Result Success and not null`() {
        coTest {
            registerViewModel = RegisterViewModel(successUseCase)
            registerViewModel.doRegisterUser(TestHelper.provideRegisterRequest())
            val actualData = registerViewModel.registerResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeInstanceOf NetworkResult.Success::class
            actualData.data?.message shouldBeEqualTo "No Error"
            actualData.data?.error?.shouldNotBeTrue()
        }
    }

    @Test
    fun `when Network Error should return Result error`() {
        coTest {
            registerViewModel = RegisterViewModel(errorUseCase)
            registerViewModel.doRegisterUser(TestHelper.provideRegisterRequest())
            val actualData = registerViewModel.registerResult.getOrAwaitValue()
            actualData.shouldNotBeNull()
            actualData shouldBeEqualTo NetworkResult.Error::class
            actualData.message shouldBeEqualTo SingleEvent("network is error")
        }
    }
}