package com.joo.miruni.domain.usecase.setting

import com.joo.miruni.domain.repository.SharedPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingObserveUnlockStateUseCaseImpl @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
) : SettingObserveUnlockStateUseCase {
    override suspend fun invoke(): Flow<Boolean> {
        return sharedPreferenceRepository.observeSettingUnlockState()
    }
}