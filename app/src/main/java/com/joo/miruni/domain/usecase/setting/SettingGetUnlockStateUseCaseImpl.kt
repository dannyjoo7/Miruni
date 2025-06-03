package com.joo.miruni.domain.usecase.setting

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SettingGetUnlockStateUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingGetUnlockStateUseCase {
    override fun invoke(): Boolean {
        return sharedPreferenceRepository.getSettingUnlockScreenState()
    }
}