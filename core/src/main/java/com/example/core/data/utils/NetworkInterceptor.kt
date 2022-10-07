package com.example.core.data.utils

import android.util.Log
import com.example.core.data.local.PreferencesDataStore
import com.example.core.di.ApplicationScope
import com.example.core.di.CoroutinesQualifier
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInterceptor @Inject constructor(
    @ApplicationScope
    private val externalScope: CoroutineScope,
    @CoroutinesQualifier.MainDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val prefs: PreferencesDataStore
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var token = ""
        val modifiedRequest = originalRequest.newBuilder()
        if (originalRequest.url.toString().contains("stories")) {
            runBlocking {
                prefs.getUserToken()?.let {
                    token = it
                }
                modifiedRequest.addHeader("Authorization", "Bearer " + token)
            }
        }
        return chain.proceed(modifiedRequest.build())
    }
}