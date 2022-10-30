package com.example.submission1androidintermediate.helper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
abstract class CoroutinesTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    val testScope = TestScope()
    protected val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

    @Before
    fun setupViewModelScope() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanupViewModelScope() {
        Dispatchers.resetMain()
    }

    fun coTest(block: suspend TestScope.() -> Unit) =
        testScope.runBlockingTest(block)
}