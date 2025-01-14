package com.joo.miruni.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingObserveUnlockStateUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}
