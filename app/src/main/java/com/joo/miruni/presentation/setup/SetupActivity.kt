package com.joo.miruni.presentation.setup

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.joo.miruni.presentation.main.MainActivity
import com.joo.miruni.service.ForegroundService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupActivity : ComponentActivity() {

    private val setupViewModel: SetupViewModel by viewModels()

    // 알림을 보내기 위한 권한 요청
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                initModel()
            } else {
                // 권한이 거부되었을 때의 처리
                Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        window.statusBarColor = android.graphics.Color.WHITE

        // 권한 요청
        requestPermissions()

        initModel()

        setContent {
            SetupScreen(setupViewModel)
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // POST_NOTIFICATIONS 권한 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initModel() = with(setupViewModel) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // POST_NOTIFICATIONS 권한 확인
            if (ContextCompat.checkSelfPermission(
                    this@SetupActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                startForegroundService()
            }
        } else {
            startForegroundService()
        }


        // 설정 확인
        isInit.observe(this@SetupActivity) { isInit ->
            if (!isInit) {
//                startUnlockService()
                navigateToActivityMainActivity()
            }
        }
    }

    private fun navigateToActivityMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
