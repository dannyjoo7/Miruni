package com.joo.miruni.presentation.main

import androidx.lifecycle.ViewModel
import com.joo.miruni.R
import com.joo.miruni.presentation.BottomNavItem
import com.joo.miruni.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "MainViewModel"
    }

    // 아이콘 리소스 ID만 저장
    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem("미루기", R.drawable.ic_clock, Screen.PutOff),
        BottomNavItem("홈", R.drawable.ic_home, Screen.Home),
        BottomNavItem("캘린더", R.drawable.ic_calendar, Screen.Calendar)
    )
}



