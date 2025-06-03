package com.joo.miruni.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joo.miruni.R
import com.joo.miruni.domain.usecase.setting.SettingActiveUnlockScreenUseCase
import com.joo.miruni.domain.usecase.setting.SettingCompletedItemsVisibilityUseCase
import com.joo.miruni.domain.usecase.setting.SettingGetCompletedItemsVisibilityStateUseCase
import com.joo.miruni.domain.usecase.setting.SettingGetUnlockStateUseCase
import com.joo.miruni.presentation.BottomNavItem
import com.joo.miruni.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingCompletedItemsVisibilityUseCase: SettingCompletedItemsVisibilityUseCase,
    private val settingActiveUnlockScreenUseCase: SettingActiveUnlockScreenUseCase,
    private val settingGetCompletedItemsVisibilityStateUseCase: SettingGetCompletedItemsVisibilityStateUseCase,
    private val settingGetUnlockStateUseCase: SettingGetUnlockStateUseCase,
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    /*
    * Live Data
    * */

    // 완료 항목 값
    private val _settingObserveCompleteVisibility = MutableLiveData<Boolean>(false)
    val settingObserveCompleteVisibility: LiveData<Boolean> get() = _settingObserveCompleteVisibility

    // 잠금화면 활성화 여부
    private val _settingObserveUnlockState = MutableLiveData<Boolean>(true)
    val settingObserveUnlockState: LiveData<Boolean> get() = _settingObserveUnlockState

    init {
        loadUserSetting()
    }

    // 아이콘 리소스 ID만 저장
    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem("미루기", R.drawable.ic_clock, Screen.Overdue),
        BottomNavItem("홈", R.drawable.ic_home, Screen.Home),
        BottomNavItem("캘린더", R.drawable.ic_calendar, Screen.Calendar)
    )

    // 완료된 항목 보기 설정
    fun setCompletedItemsVisibility() {
        viewModelScope.launch {
            runCatching {
                settingCompletedItemsVisibilityUseCase.invoke()
            }.onSuccess {
                _settingObserveCompleteVisibility.value =
                    _settingObserveCompleteVisibility.value!!.not()
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load settings", exception)
            }
        }
    }

    // 잠금 화면 활성화 / 비활성화 기능
    fun setActiveUnlockScreen() {
        viewModelScope.launch {
            runCatching {
                settingActiveUnlockScreenUseCase.invoke()
            }.onSuccess {
                _settingObserveUnlockState.value =
                    _settingObserveUnlockState.value!!.not()
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load settings", exception)
            }
        }
    }

    // 유저 설정 로드
    private fun loadUserSetting() {
        viewModelScope.launch {
            // 완료된 아이템 가시성 상태 로드
            runCatching {
                settingGetCompletedItemsVisibilityStateUseCase.invoke()
            }.onSuccess { visibility ->
                _settingObserveCompleteVisibility.value = visibility
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load completed items visibility", exception)
            }

            // 잠금 상태 로드
            runCatching {
                settingGetUnlockStateUseCase.invoke()
            }.onSuccess { unlockState ->
                _settingObserveUnlockState.value = unlockState
            }.onFailure { exception ->
                Log.e(TAG, "Failed to load unlock state", exception)
            }
        }
    }
}



