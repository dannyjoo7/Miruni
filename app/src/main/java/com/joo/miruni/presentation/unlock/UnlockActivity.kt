package com.joo.miruni.presentation.unlock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import com.joo.miruni.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnlockActivity : ComponentActivity() {
    private val unlockViewModel: UnlockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)

        setContent {
            UnlockScreen(unlockViewModel)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼 비활성화
            }
        })
    }
}
