package com.example.core.di

import com.example.core.data.source.user.UserDataSource
import com.example.core.data.source.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: UserDataSource): UserRepository {
        return UserRepository(remoteDataSource)
    }
}