package com.example.submission1androidintermediate.helper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class CoroutinesTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    protected val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupViewModelScope() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanupViewModelScope() {
        Dispatchers.resetMain()
    }

}