package com.joo.miruni.domain.usecase.setting

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingObserveCompletedItemsVisibilityUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingObserveCompletedItemsVisibilityUseCase {
    override suspend fun invoke(): Flow<Boolean> {
        return sharedPreferenceRepository.observeSettingCompletedItemsVisibility()
    }
}