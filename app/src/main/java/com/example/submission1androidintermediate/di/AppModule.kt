package com.example.submission1androidintermediate.di

import com.example.domain.usecase.user.UserUseCase
import com.example.submission1androidintermediate.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class AppModule {


    @Provides
    @ActivityScoped
    fun provideDetailMovieFragmentPresenter(
        useCase: UserUseCase
    ): LoginViewModel {
        return LoginViewModel(useCase)
    }

}