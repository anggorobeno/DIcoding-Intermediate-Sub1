package com.example.domain.di

import com.example.domain.repository.stories.IStoriesRepository
import com.example.domain.repository.user.IUserRepository
import com.example.domain.usecase.stories.StoriesInteractor
import com.example.domain.usecase.stories.StoriesUseCase
import com.example.domain.usecase.user.UserInteractor
import com.example.domain.usecase.user.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideUserInteractor(repository: IUserRepository): UserUseCase {
        return UserInteractor(repository)
    }

    @Provides
    @Singleton
    fun provideStoriesInteractor(repository: IStoriesRepository): StoriesUseCase {
        return StoriesInteractor(repository)
    }

}