package com.joo.miruni.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingObserveCompletedItemsVisibilityUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}
