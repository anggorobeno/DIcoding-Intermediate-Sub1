package com

import android.app.Application
import com.example.submission1androidintermediate.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
            com.github.ajalt.timberkt.Timber.plant(com.github.ajalt.timberkt.Timber.DebugTree())
        }
        com.github.ajalt.timberkt.Timber.d {
            "Application onCreate called"
        }
    }
}