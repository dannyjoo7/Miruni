package com.joo.miruni.presentation.main


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    // 알림을 보내기 위한 권한 요청
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startForegroundService()
            } else {
                Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

    private var showPermissionDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        window.statusBarColor = Color.WHITE
        enableEdgeToEdge()

        requestPermissions()

        setContent {
            if (showPermissionDialog) {
                BasicDialog(
                    dialogType = DialogMod.PERMISSION,
                    showDialog = showPermissionDialog,
                    onDismiss = {
                        showPermissionDialog = false
                        finish()
                    },
                    onCancel = {
                        showPermissionDialog = false
                        finish()
                    },
                    onConfirmed = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    title = "알림 권한"
                )
            }

            val navController = rememberNavController()
            MainScreen(navController, mainViewModel)
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
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    showPermissionDialog = true
                } else {
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                startForegroundService()
            }
        }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}
