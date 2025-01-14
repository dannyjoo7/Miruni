package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingGetUnlockStateUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingGetUnlockStateUseCase {
    override fun invoke(): Boolean {
        return sharedPreferenceRepository.getSettingUnlockScreenState()
    }
}