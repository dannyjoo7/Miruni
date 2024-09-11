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
import com.ozcanalasalvar.datepicker.compose.timepicker.WheelTimePicker
import com.ozcanalasalvar.datepicker.model.Time


@Composable
fun SetupScreen(setupViewModel: SetupViewModel) {
    /*
    * 뷰모델 인스턴스 가져오기
    * */

    val selectedTime by setupViewModel.selectedTime.observeAsState("09 : 00 AM") // 선택된 시간
    var showTimePicker by remember { mutableStateOf(false) } // TimePicker 표시 여부
    var isSelect by remember { mutableStateOf(false) } // 기상 시간 선택 여부

    // 기본 선택 시간을 오전 9시로 설정
    val startTime = Time(hour = 9, minute = 0, format = "AM") // 오전 9시 설정

    // 전체 화면을 차지하도록 Box 사용
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
                .padding(bottom = 80.dp), // 하단 버튼을 위한 여백
            verticalArrangement = Arrangement.Top, // 위쪽 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 가로 방향 중앙 정렬
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
                    .background(Color.Transparent) // 배경을 투명하게 설정
            ) {
                Text(
                    text = "기상시간 입력",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Center) // 텍스트를 박스의 중앙에 정렬
                        .padding(8.dp) // 텍스트 주위에 여유 공간 추가
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
            modifier = Modifier.align(Alignment.Center), // 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 가로 방향 중앙 정렬
        ) {
            Button(
                onClick = {
                    showTimePicker = !showTimePicker // TimePicker 표시/숨기기
                    isSelect = false // 기상 시간 선택
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // 배경색을 투명하게 설정
                    contentColor = Color.Gray // 텍스트 색상 설정
                ),
                elevation = null, // 그림자 없애기
                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp), // 테두리 모서리 설정
                modifier = Modifier
                    .padding(top = 16.dp) // 필요에 따라 여백 설정
            ) {
                Text(
                    text = selectedTime.ifEmpty { "AM 9 : 00" },
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    color = Color.Gray
                )
            }

            // WheelTimePicker 위젯 표시
            AnimatedVisibility(visible = showTimePicker) {
                Column(
                    modifier = Modifier
                        .animateContentSize() // 크기 변화에 따른 애니메이션 적용
                        .offset(y = 16.dp), // 버튼 아래에 위치하도록 여백 추가
                    horizontalAlignment = Alignment.CenterHorizontally // 가로 방향 중앙 정렬
                ) {
                    WheelTimePicker(
                        offset = 2, // 표시할 항목 수
                        timeFormat = TimeFormat.CLOCK_12H, // 12시간 형식
                        startTime = startTime, // 기본 선택 시간 (오전 9시)
                        textSize = 19, // 텍스트 크기
                        selectorEffectEnabled = true, // 선택 효과 사용
                        darkModeEnabled = false, // 다크 모드 비활성화
                        onTimeChanged = { hour, minute, format ->
                            // 시간 변경 시 처리
                            setupViewModel.updateSelectedTime(
                                String.format(
                                    "%s %d : %02d",
                                    format,
                                    hour,
                                    minute,
                                )
                            )
                        }
                    )

                    // 시간 선택 완료 버튼
                    Button(
                        onClick = {
                            showTimePicker = false // TimePicker 숨기기
                            isSelect = true // 기상 시간 선택 완료
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007AFF), // 배경 색상을 파란색으로 설정
                            contentColor = Color.White,  // 텍스트 색상을 흰색으로 설정
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
                            color = Color.White // 텍스트 색상 흰색으로 설정
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
                .align(Alignment.BottomCenter), // 하단 중앙 정렬
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = isSelect) {
                Button(
                    onClick = {
                        setupViewModel.saveAlarmTime()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF), // 배경 색상을 파란색으로 설정
                        contentColor = Color.White,  // 텍스트 색상을 흰색으로 설정
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
                        color = Color.White // 텍스트 색상 흰색으로 설정
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
//    val previewViewModel = SetupViewModel()
//
//    SetupScreen(previewViewModel)
//}




