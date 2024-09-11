package com.joo.miruni.di

import com.joo.miruni.domain.usecase.GetAlarmTimeUseCase
import com.joo.miruni.domain.usecase.GetAlarmTimeUseCaseImpl
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCase
import com.joo.miruni.domain.usecase.SaveAlarmTimeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    abstract fun bindSaveAlarmTimeUseCase(
        saveAlarmTimeUseCaseImpl: SaveAlarmTimeUseCaseImpl
    ): SaveAlarmTimeUseCase

    @Binds
    @Singleton
    abstract fun bindGetAlarmTimeUseCase(
        getAlarmTimeUseCaseImpl: GetAlarmTimeUseCaseImpl
    ): GetAlarmTimeUseCase
}

