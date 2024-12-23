package com.joo.miruni.presentation.addTask.addSchedule


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleActivity : ComponentActivity() {
    private val addScheduleViewModel: AddScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        WindowCompat.setDecorFitsSystemWindows(window, false) // 엣지 투 엣지 모드
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val navController = rememberNavController()
            AddScheduleScreen(addScheduleViewModel)
        }
    }
}