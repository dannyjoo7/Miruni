package com.joo.miruni.domain.usecase.setting

import kotlinx.coroutines.flow.Flow

interface SettingObserveCompletedItemsVisibilityUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}
