package com

import android.app.Application
import com.example.submission1androidintermediate.BuildConfig
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.DebugTree
import com.github.ajalt.timberkt.Timber.plant
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
        Timber.d {
            "Application onCreate called"
        }
    }
}