package com.joo.miruni.domain.usecase.setting

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import javax.inject.Inject

class SettingActiveUnlockScreenUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingActiveUnlockScreenUseCase {
    override suspend fun invoke() {
        sharedPreferenceRepository.settingActiveUnlockScreen()
    }
}