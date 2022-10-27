package com.example.core.data.utils

import com.example.core.data.local.IDataStore
import com.example.core.di.CoroutinesQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInterceptor @Inject constructor(
    @CoroutinesQualifier.MainDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val prefs: IDataStore
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