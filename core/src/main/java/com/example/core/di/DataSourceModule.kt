package com.example.core.di

import com.example.core.data.remote.services.DicodingStoryApiService
import com.example.core.data.source.stories.StoriesDataSource
import com.example.core.data.source.stories.StoriesPagingSource
import com.example.core.data.source.stories.StoriesRemoteDataSourceImpl
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
    fun provideUserRemoteDataSource(apiService: DicodingStoryApiService): UserDataSource {
        return UserRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideStoriesRemoteDataSource(apiService: DicodingStoryApiService): StoriesDataSource {
        return StoriesRemoteDataSourceImpl(apiService)
    }

    @Provides
    fun provideStoriesPagingSource(apiService: DicodingStoryApiService): StoriesPagingSource {
        return StoriesPagingSource(apiService)
    }
}
