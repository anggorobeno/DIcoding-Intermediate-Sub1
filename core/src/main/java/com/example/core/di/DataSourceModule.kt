package com.example.core.di

import com.example.core.data.remote.services.DicodingStoryApiService
import com.example.core.data.source.user.UserDataSource
import com.example.core.data.source.user.UserRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {


    @Provides
    @Singleton
    fun provideUserDataSource(apiService: DicodingStoryApiService): UserDataSource {
        return UserRemoteDataSourceImpl(apiService)
    }
}
