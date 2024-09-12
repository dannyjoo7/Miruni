package com.joo.miruni.presentation.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        WindowCompat.setDecorFitsSystemWindows(window, false) // 엣지 투 엣지 모드
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val navController = rememberNavController()
            MainScreen(navController, mainViewModel)
        }
    }
}