package com.example.submission1androidintermediate.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.usecase.user.UserUseCase
import com.example.submission1androidintermediate.helper.MainCoroutineRule
import com.example.submission1androidintermediate.ui.register.RegisterViewModel
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule

class LoginViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var successUseCase: UserUseCase
    private lateinit var errorUseCase: UserUseCase
    private lateinit var successLoginViewModel: LoginViewModel
    private lateinit var errorLoginViewModel: LoginViewModel

    @Before
    fun setUp() {

    }
}