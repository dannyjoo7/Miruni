package com.joo.miruni.presentation.widget

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.joo.miruni.R
import java.time.LocalDate

@Composable
fun DateRangePicker(
    context: Context,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    onDateSelected: (LocalDate?, LocalDate?) -> Unit,
    onDismiss: () -> Unit,
) {
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    var currentDate by remember { mutableStateOf(LocalDate.now()) }

    // 달력 렌더링
    val renderCalendar: @Composable () -> Unit = {
        val year = currentDate.year
        val month = currentDate.monthValue
        val daysInMonth = currentDate.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(year, month, 1).dayOfWeek.value % 7
        val today = LocalDate.now()

        Column {
            // 주간 헤더
            Row {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.ios_gray_calander_font)
                    )
                }
            }

            for (week in 0..5) {
                Row {
                    for (day in 0..6) {
                        val dayOfMonth = week * 7 + day - firstDayOfMonth + 1
                        if (dayOfMonth in 1..daysInMonth) {
                            val currentDay = LocalDate.of(year, month, dayOfMonth)
                            val isToday = currentDay.isEqual(today)

                            // 해당 날짜가 어느 유형으로 선택됐는가
                            val isStartDateSelected =
                                selectedStartDate?.dayOfMonth == dayOfMonth && selectedStartDate.monthValue == month
                            val isEndDateSelected =
                                selectedEndDate?.dayOfMonth == dayOfMonth && selectedEndDate.monthValue == month

                            // 선택 범위 안에 있는가
                            val isInRange = selectedStartDate != null && selectedEndDate != null &&
                                    !currentDay.isBefore(selectedStartDate) && !currentDay.isAfter(
                                selectedEndDate
                            )

                            // StartDate 이전에 날짜인가
                            val isBeforeStartDate =
                                selectedStartDate != null && currentDay.isBefore(selectedStartDate)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .background(
                                        when {
                                            isStartDateSelected -> colorResource(id = R.color.ios_light_blue)
                                            isEndDateSelected -> colorResource(id = R.color.ios_light_blue)
                                            isInRange -> colorResource(id = R.color.ios_light_blue)
                                            else -> Color.Transparent
                                        },
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .clickable(enabled = !isBeforeStartDate) {
                                        when {
                                            // 시작일 선택
                                            selectedStartDate == null -> {
                                                onDateSelected(currentDay, null)
                                            }
                                            // 종료일 선택
                                            selectedEndDate == null -> {
                                                // 종료일이 시작일보다 이전인 경우
                                                if (currentDay.isBefore(selectedStartDate)) {
                                                    onDateSelected(currentDay, null) // 시작일로 재선택
                                                } else {
                                                    // 정상 종료일 선택
                                                    onDateSelected(selectedStartDate, currentDay)
                                                }
                                            }
                                            // 이미 시작일과 종료일이 선택된 경우
                                            else -> {
                                                onDateSelected(null, null)
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayOfMonth.toString(),
                                    color = when {
                                        isStartDateSelected || isEndDateSelected -> colorResource(id = R.color.ios_blue)
                                        isBeforeStartDate -> Color.Gray
                                        isToday -> colorResource(id = R.color.ios_blue)
                                        else -> Color.Black
                                    },
                                    fontSize = when {
                                        isStartDateSelected || isEndDateSelected -> 20.sp
                                        else -> 18.sp
                                    }
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentWidth()
            .widthIn(max = 500.dp, min = 400.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(ContextCompat.getColor(context, R.color.gray_menu))
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 선택된 날짜 표시 텍스트
                Text(
                    text = "${currentDate.monthValue}월 ${currentDate.year}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )

                // 월 변경 버튼
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    IconButton(onClick = { currentDate = currentDate.minusMonths(1) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_left),
                            contentDescription = "Previous Month",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(id = R.color.ios_blue),
                        )
                    }
                    IconButton(onClick = { currentDate = currentDate.plusMonths(1) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_right),
                            contentDescription = "Next Month",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(id = R.color.ios_blue)
                        )
                    }
                }
            }

            // 달력 렌더링
            renderCalendar()

            // 두 날짜 선택 완료 시 확인 버튼
            if (selectedStartDate != null && selectedEndDate != null) {
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.ios_blue),
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "확인")
                }
            }
        }
    }
}