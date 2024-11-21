package com.joo.miruni.presentation.setup

import android.app.AlarmManager
import android.content.Context
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupActivity : ComponentActivity() {
    private val setupViewModel: SetupViewModel by viewModels()

    // 알림을 보내기 위한 권한 요청
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허용되었을 때 모델 초기화
                initModel()
            } else {
                // 권한이 거부되었을 때의 처리
                Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    // TODO 작동이 안됨
    // 정확한 시간에 알람을 예약하기 위한 권한 요청
    private val requestExactAlarmPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허용되었을 때의 처리
                Toast.makeText(this, "정확한 알람 권한 O", Toast.LENGTH_SHORT).show()
            } else {
                // 권한이 거부되었을 때의 처리
                Toast.makeText(this, "정확한 알람 권한 X", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        window.statusBarColor = android.graphics.Color.WHITE

        // 권한 요청
        requestPermissions()

        setContent {
            SetupScreen(setupViewModel)
        }
    }

    private fun requestPermissions() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // POST_NOTIFICATIONS 권한 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // API 31 이상
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!alarmManager.canScheduleExactAlarms()) {
                        requestExactAlarmPermissionLauncher.launch(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
                    } else {
                        initModel()
                    }
                } else {
                    // API 30 이하
                    initModel()
                }
            }
        } else {
            // Android 12 이하
            initModel()
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
        finish()
    }
}
