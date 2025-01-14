package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SettingGetCompletedItemsVisibilityStateUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingGetCompletedItemsVisibilityStateUseCase {
    override fun invoke(): Boolean {
        return sharedPreferenceRepository.getSettingCompletedItemsVisibilityState()
    }
}