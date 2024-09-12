package com.joo.miruni.presentation.setup


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.joo.miruni.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupActivity : ComponentActivity() {
    private val setupViewModel: SetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        WindowCompat.setDecorFitsSystemWindows(window, false) // 엣지 투 엣지 모드
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        initModel()

        setContent {
            SetupScreen(setupViewModel)
        }
    }

    private fun initModel() = with(setupViewModel) {
        isInit.observe(this@SetupActivity) { isInit ->
            if (!isInit) {
                navigateToActivityMainActivity()
            }
        }
    }

    private fun navigateToActivityMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // SetupActivity를 종료할 경우
    }
}
