package com.example.domain.di

import com.example.core.data.source.user.UserRepository
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
    fun provideReviewInteractor(repository: UserRepository): UserUseCase {
        return UserInteractor(repository)
    }

}