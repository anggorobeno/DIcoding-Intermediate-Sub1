package com.example.core.data.utils

import android.util.Log
import com.example.core.data.local.PreferencesDataStore
import com.example.core.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkInterceptor @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val prefs: PreferencesDataStore
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var token = ""
        var modifiedRequest: Request.Builder
        if (originalRequest.url.toString().contains("stories")) {
            Log.d("TAG", "intercept: ")
            externalScope.launch {
                Log.d("TAG", "intercept: ")
                prefs.getUserToken()?.let {
                    Log.d("TAG", "intercept: $it")
                    token = it
                }
            }
            modifiedRequest = originalRequest.newBuilder()
        } else modifiedRequest = originalRequest.newBuilder()
        return chain.proceed(modifiedRequest.build())
    }
}