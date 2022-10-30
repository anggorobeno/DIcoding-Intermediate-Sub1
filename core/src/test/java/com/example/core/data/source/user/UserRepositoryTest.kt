package com.example.core.data.source.user

import app.cash.turbine.test
import com.example.core.helper.TestHelper
import com.example.domain.utils.NetworkResult
import com.example.submission1androidintermediate.helper.CoroutinesTest
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    //SUT
    private lateinit var userRepository: UserRepository

    private lateinit var userDataSource: UserDataSource

    @Before
    fun setUp() {
        userDataSource = mock()
        userRepository = UserRepository(userDataSource)
    }

    @Test
    fun `when calling loginUser should first emit loading and success`() {
        runTest {
            whenever(userDataSource.loginUser(TestHelper.provideLoginRequest())).thenReturn(
                Response.success(TestHelper.provideLoginResponse())
            )
            val actualData = userRepository.loginUser(TestHelper.provideLoginRequest())
            actualData.test {

                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Success::class

                awaitComplete()
            }
            verify(userDataSource).loginUser(TestHelper.provideLoginRequest())

        }
    }

    @Test
    fun `when calling loginUser with network error should emit loading and error`() {
        runTest {
            whenever(userDataSource.loginUser(TestHelper.provideLoginRequest())).thenReturn(
                Response.error(500, TestHelper.provideErrorResponse())
            )
            val actualData = userRepository.loginUser(TestHelper.provideLoginRequest())
            actualData.test {

                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val errorItem = awaitItem()

                errorItem shouldBeInstanceOf NetworkResult.Error::class

                awaitComplete()

            }
            verify(userDataSource).loginUser(TestHelper.provideLoginRequest())
        }
    }

    @Test
    fun `when calling registerUser should first emit loading and success`() {
        runTest {
            whenever(userDataSource.registerUser(TestHelper.provideRegisterRequest())).thenReturn(
                Response.success(TestHelper.provideRegisterResponse())
            )
            val actualData = userRepository.registerUser(TestHelper.provideRegisterRequest())
            actualData.test {

                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Success::class

                awaitComplete()
            }
            verify(userDataSource).registerUser(TestHelper.provideRegisterRequest())

        }
    }

    @Test
    fun `when calling registerUser with network error should return loading and error`() {
        runTest {
            whenever(userDataSource.registerUser(TestHelper.provideRegisterRequest())).thenReturn(
                Response.error(500, TestHelper.provideErrorResponse())
            )
            val actualData = userRepository.registerUser(TestHelper.provideRegisterRequest())
            actualData.test {

                val loading = awaitItem()
                loading shouldBeInstanceOf NetworkResult.Loading::class

                val item = awaitItem()
                item shouldBeInstanceOf NetworkResult.Error::class

                awaitComplete()
            }
            verify(userDataSource).registerUser(TestHelper.provideRegisterRequest())
        }
    }
}