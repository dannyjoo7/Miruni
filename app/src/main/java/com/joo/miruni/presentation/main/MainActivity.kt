package com.joo.miruni.presentation.main


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.joo.miruni.presentation.widget.BasicDialog
import com.joo.miruni.presentation.widget.DialogMod
import com.joo.miruni.service.ForegroundService
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    // 알림을 보내기 위한 권한 요청
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                notificationPermission = true
                startForegroundService()
            } else {
                Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    // 포그라운드 서비스를 위한 권한 요청
    private val requestForegroundServicePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                foregroundServicePermission = true
                startForegroundService()
            } else {
                Toast.makeText(this, "포그라운드 서비스 설정이 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private var notificationPermission by mutableStateOf(false)
    private var foregroundServicePermission by mutableStateOf(false)

    private var showPostNotificationPermissionDialog by mutableStateOf(false)
    private var showForegroundServicePermissionDialog by mutableStateOf(false)
    private var showBatterySettingPermissionDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.WHITE, Color.WHITE
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.WHITE, Color.WHITE
            )
        )

        requestPermissions()

        setContent {
            val navController = rememberNavController()
            MainScreen(navController, mainViewModel)

            // 알림 권한 설정 다이얼로그
            if (showPostNotificationPermissionDialog) {
                BasicDialog(
                    dialogType = DialogMod.POST_NOTIFICATION_PERMISSION,
                    showDialog = showPostNotificationPermissionDialog,
                    onDismiss = {
                        showPostNotificationPermissionDialog = false
                        if (!notificationPermission) {
                            finish()
                        }
                    },
                    onCancel = {
                        showPostNotificationPermissionDialog = false
                        if (!notificationPermission) {
                            finish()
                        }
                    },
                    onConfirmed = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    title = "알림 권한"
                )
            }

            // 포그라운드 권한 설정 다이얼로그
            if (!showPostNotificationPermissionDialog && showForegroundServicePermissionDialog) {
                BasicDialog(
                    dialogType = DialogMod.FOREGROUND_SERVICE_PERMISSION,
                    showDialog = showForegroundServicePermissionDialog,
                    onDismiss = {
                        showForegroundServicePermissionDialog = false
                        if (!foregroundServicePermission) {
                            finish()
                        }
                    },
                    onCancel = {
                        showForegroundServicePermissionDialog = false
                        if (!foregroundServicePermission) {
                            finish()
                        }
                    },
                    onConfirmed = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                requestForegroundServicePermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE)
                            }
                        }
                    },
                    title = "알림 권한"
                )
            }

            // 배터리 권한 설정 다이얼로그
            if (!showPostNotificationPermissionDialog && !showForegroundServicePermissionDialog && showBatterySettingPermissionDialog) {
                BasicDialog(
                    dialogType = DialogMod.BATTERY_SETTING_PERMISSION,
                    showDialog = showBatterySettingPermissionDialog,
                    onDismiss = {
                        showBatterySettingPermissionDialog = false
                        finish()
                    },
                    onCancel = {
                        showBatterySettingPermissionDialog = false
                        finish()
                    },
                    onConfirmed = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            startActivity(
                                Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                            )
                        }
                    },
                    title = "배터리 최적화 설정"
                )
            }
        }
    }

    // 필수 권한 요청
    private fun requestPermissions() {
        // 알람 표시 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    showPostNotificationPermissionDialog = true
                } else {
                    showPostNotificationPermissionDialog = true
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                notificationPermission = true
                startForegroundService()
            }
        }

        // 배터리 최적화 설정
        if (!(getSystemService(Context.POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                packageName
            )
        ) {
            showBatterySettingPermissionDialog = true
        }
    }

    // 포그라운드 서비스 시작
    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
