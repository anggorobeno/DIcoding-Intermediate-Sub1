package com.example.core.di

import javax.inject.Qualifier

class CoroutinesQualifier {
    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class DefaultDispatcher

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class IoDispatcher

    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class MainDispatcher

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class MainImmediateDispatcher
}