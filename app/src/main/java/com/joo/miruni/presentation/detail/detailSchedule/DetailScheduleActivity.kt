package com.joo.miruni.presentation.detail.detailSchedule


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailScheduleActivity : ComponentActivity() {
    private val detailScheduleViewModel: DetailScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scheduleId = intent.getLongExtra("SCHEDULE_ID", -1)

        if (scheduleId == -1L) {
            Toast.makeText(this.applicationContext, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            initModel(scheduleId)
        }

        // 상태바 설정
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val navController = rememberNavController()
            DetailScheduleScreen(detailScheduleViewModel)
        }
    }

    private fun initModel(scheduleId: Long) {
        detailScheduleViewModel.loadScheduleDetails(scheduleId)
    }
}