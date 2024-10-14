package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SettingCompletedItemsVisibilityUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingCompletedItemsVisibilityUseCase {
    override suspend fun invoke() {
        sharedPreferenceRepository.settingUpdateCompletedItemsVisibility()
    }
}