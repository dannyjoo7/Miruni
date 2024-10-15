package com.joo.miruni.presentation.setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import com.google.android.material.timepicker.TimeFormat
import com.joo.miruni.presentation.widget.Time
import com.joo.miruni.presentation.widget.WheelTimePicker
import java.time.LocalTime


@Composable
fun SetupScreen(setupViewModel: SetupViewModel) {

    /*
    * Live Data
    * */
    val selectedTime by setupViewModel.selectedTime.observeAsState()        // 선택된 시간
    val showTimePicker by setupViewModel.showTimePicker.observeAsState()    // TimePicker 표시 여부

    var isSelect by remember { mutableStateOf(false) }                // 기상 시간 선택 여부

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 상단 부분 (기상시간 입력 박스와 설명)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 평균 기상 시간 입력 박스
            Box(
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 16.dp)
                    .defaultMinSize(minWidth = 250.dp)
                    .border(
                        0.5.dp,
                        Color.Gray,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .background(Color.Transparent)
            ) {
                Text(
                    text = "기상시간 입력",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }

            // 설명 문구
            Text(
                text = "기상 시간에 맞춰 당일의 알림을 띄워드려요",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        // 중앙에 위치할 시간 선택 버튼
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    setupViewModel.toggleTimePickerBtn()
                    isSelect = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Gray
                ),
                elevation = null,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp),
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = setupViewModel.formatLocalTimeToString(
                        selectedTime ?: LocalTime.now()
                    ),
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    color = Color.Gray
                )
            }

            // WheelTimePicker 위젯 표시
            AnimatedVisibility(visible = showTimePicker ?: false) {
                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .offset(y = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WheelTimePicker(
                        offset = 2,
                        selectorEffectEnabled = true,
                        timeFormat = TimeFormat.CLOCK_12H,
                        startTime = setupViewModel.convertLocalTimeToTime(
                            selectedTime ?: LocalTime.now()
                        ),
                        textSize = 19,
                        onTimeChanged = { hour, minute, format ->
                            setupViewModel.updateSelectedTime(
                                hour,
                                minute,
                                format ?: "오전"
                            )
                        },
                        darkModeEnabled = false,
                    )

                    // 시간 선택 완료 버튼
                    Button(
                        onClick = {
                            setupViewModel.toggleTimePickerBtn()
                            isSelect = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007AFF),
                            contentColor = Color.White,
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(24.dp)
                            .defaultMinSize(minWidth = 250.dp)
                    ) {
                        Text(
                            text = "기상시간 입력",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 완료 버튼
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = isSelect) {
                Button(
                    onClick = {
                        setupViewModel.saveAlarmTime()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF),
                        contentColor = Color.White,
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "완료",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SetupScreenPreview() {
//    // 미리보기용으로 ViewModel을 직접 생성
//    val previewViewModel = SetupViewModel(
//
//    )
//
//    SetupScreen(previewViewModel)
//}




