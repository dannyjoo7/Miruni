package com.joo.miruni.presentation.putoff

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PutOffScreen(
    putOffViewModel: PutOffViewModel = hiltViewModel(),
) {
    // 미루기 화면의 내용
    Text("미루기 화면")
}
