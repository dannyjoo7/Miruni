package com.joo.miruni.presentation.addTodo

import android.app.Activity
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joo.miruni.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.material.timepicker.TimeFormat
import com.joo.miruni.presentation.widget.AlarmDisplayDatePicker
import com.joo.miruni.presentation.widget.Time
import com.joo.miruni.presentation.widget.WheelTimePicker
import java.time.LocalDate
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    addTodoViewModel: AddTodoViewModel = hiltViewModel(),
) {
    // 현재 컨택스트
    val context = LocalContext.current

    // 키보드 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current


    /*
    * Live Data
    *  */
    val todoText by addTodoViewModel.todoText.observeAsState("")
    val descriptionText by addTodoViewModel.descriptionText.observeAsState("")

    val showDatePicker by addTodoViewModel.showDatePicker.observeAsState(false)
    val showTimePicker by addTodoViewModel.showTimePicker.observeAsState(false)
    val showAlarmDisplayStartDatePicker by addTodoViewModel.showAlarmDisplayStartDatePicker.observeAsState(
        true
    )

    val selectDate by addTodoViewModel.selectedDate.observeAsState()
    val selectTime by addTodoViewModel.selectedTime.observeAsState()
    val selectedAlarmDisplayDate by addTodoViewModel.selectedAlarmDisplayDate.observeAsState()

    val isTodoTextEmpty by addTodoViewModel.isTodoTextEmpty.observeAsState(false)
    val isTodoAddedSuccess by addTodoViewModel.isTodoAdded.observeAsState(false)

    /*
    * 애니매이션
    * */
    val shakeAnimation by animateFloatAsState(
        targetValue = if (isTodoTextEmpty) 30f else 0f,
        animationSpec = repeatable(
            iterations = 6,
            animation = tween(
                durationMillis = 100,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "Shaking Animation"
    )
    val shakeOffset = if (isTodoTextEmpty) shakeAnimation else 0f


    // Todo추가 성공 시 해당 액티비티 종료
    LaunchedEffect(isTodoAddedSuccess) {
        if (isTodoAddedSuccess) {
            (context as? Activity)?.finish()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "할 일",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "추가",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    addTodoViewModel.addTodoItem()
                                },
                            color = colorResource(id = R.color.ios_blue)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            (context as? Activity)?.finish()
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "menu",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 할 일 입력창
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                            .offset(x = with(LocalDensity.current) { shakeOffset.toDp() })
                    ) {
                        Text(
                            text = "할 일",
                            modifier = Modifier
                                .padding(end = 34.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        TextField(
                            value = todoText,
                            onValueChange = {
                                addTodoViewModel.updateTodoText(it)
                            },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "할 일",
                                    fontSize = 16.sp,
                                    color = if (isTodoTextEmpty) {
                                        Color.Red
                                    } else {
                                        colorResource(id = R.color.ios_gray)
                                    }
                                )

                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        )
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                }

                // 세부사항 입력창
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                    ) {
                        Text(
                            text = "세부사항",
                            modifier = Modifier
                                .padding(end = 8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        TextField(
                            value = descriptionText,
                            onValueChange = {
                                addTodoViewModel.updateDescriptionText(it)
                            },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "세부사항",
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.ios_gray)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        )
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                }

                // 마감일
                Column(
                    modifier = Modifier
                        .animateContentSize(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                    ) {
                        Text(
                            text = "마감일",
                            modifier = Modifier
                                .padding(end = 38.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        // 날짜 선택 버튼
                        Button(
                            onClick = {
                                addTodoViewModel.clickedDatePickerBtn()
                                keyboardController?.hide()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(
                                vertical = 2.dp,
                                horizontal = 8.dp
                            ),
                        ) {
                            Text(
                                text = addTodoViewModel.formatSelectedDate(
                                    selectDate ?: LocalDate.now()
                                ),
                                color = colorResource(id = R.color.ios_blue),
                                fontSize = 16.sp,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                        // 시간 선택 버튼
                        Button(
                            onClick = {
                                addTodoViewModel.clickedTimePickerBtn()
                                keyboardController?.hide()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(
                                vertical = 2.dp,
                                horizontal = 8.dp
                            ),
                        ) {
                            Text(
                                text = addTodoViewModel.formatLocalTimeToString(
                                    selectTime ?: LocalTime.now()
                                ),
                                color = colorResource(id = R.color.ios_blue),
                                fontSize = 16.sp,
                            )
                        }
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)

                    /*
                    * 날짜 선택기 & 시간 선택기
                    * */
                    Box {
                        // 날짜 선택기
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showDatePicker,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                        ) {
                            DatePicker(
                                context = context,
                                addTodoViewModel = addTodoViewModel,
                                onDateSelected = { date -> addTodoViewModel.selectDate(date) },
                                onMonthChanged = { month -> addTodoViewModel.changeMonth(month) },
                            )
                        }

                        // 시간 선택기
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showTimePicker,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                WheelTimePicker(
                                    offset = 2,
                                    selectorEffectEnabled = true,
                                    timeFormat = TimeFormat.CLOCK_12H,
                                    startTime = addTodoViewModel.convertLocalTimeToTime(
                                        selectTime ?: LocalTime.now()
                                    ),
                                    textSize = 19,
                                    onTimeChanged = { hour, minute, format ->
                                        addTodoViewModel.updateSelectedTime(
                                            hour,
                                            minute,
                                            format ?: "오전"
                                        )
                                    },
                                    darkModeEnabled = false
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        addTodoViewModel.clickedTimePickerBtn()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF007AFF),
                                        contentColor = Color.White,
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
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

                // 알림 표시일 입력창
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                    ) {
                        Text(
                            text = "알람 표시 시작일",
                            modifier = Modifier
                                .padding(end = 16.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = "${selectedAlarmDisplayDate?.amount ?: 1}${selectedAlarmDisplayDate?.unit ?: "일"} 전",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable(
                                    onClick = {
                                        addTodoViewModel.clickedAlarmDisplayStartDateText()
                                        keyboardController?.hide()
                                    }),
                            fontSize = 16.sp,
                            color = colorResource(R.color.ios_blue),

                            )
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)

                    /*
                    * 알림 표시 시작일
                    * */
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 알림 표시 시작일 선택기
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showAlarmDisplayStartDatePicker,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                                .align(Alignment.Center),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                /*
                                * 알람 표시 시작일 위젯
                                * */
                                AlarmDisplayDatePicker(
                                    selectedNumber = selectedAlarmDisplayDate?.amount ?: 1,
                                    selectedText = selectedAlarmDisplayDate?.unit ?: "주",
                                    onDurationAmountChanged = { newAmount ->
                                        addTodoViewModel.updateSelectedAlarmDisplayDate(
                                            newAmount,
                                            null
                                        )
                                    },
                                    onDurationUnitChanged = { newUnit ->
                                        addTodoViewModel.updateSelectedAlarmDisplayDate(
                                            null,
                                            newUnit
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        addTodoViewModel.clickedAlarmDisplayStartDateText()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF007AFF),
                                        contentColor = Color.White,
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
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

            }
        }
    }
}

@Composable
fun DatePicker(
    context: Context,
    addTodoViewModel: AddTodoViewModel,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (Int) -> Unit,
) {


    val selectedDate by addTodoViewModel.selectedDate.observeAsState(LocalDate.now())
    val currentDate = selectedDate ?: LocalDate.now()

    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    val months = listOf(
        "1월", "2월", "3월", "4월", "5월", "6월",
        "7월", "8월", "9월", "10월", "11월", "12월"
    )

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
                            val isSelected = selectedDate?.dayOfMonth == dayOfMonth
                            val isToday = LocalDate.of(year, month, dayOfMonth).isEqual(today)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .background(
                                        when {
                                            isSelected -> colorResource(id = R.color.ios_light_blue)
                                            else -> Color.Transparent
                                        },
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .clickable {
                                        onDateSelected(LocalDate.of(year, month, dayOfMonth))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayOfMonth.toString(),
                                    color = when {
                                        isSelected -> colorResource(id = R.color.ios_blue)
                                        isToday -> colorResource(id = R.color.ios_blue)
                                        else -> Color.Black
                                    },
                                    fontSize = when {
                                        isSelected -> 20.sp
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
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(ContextCompat.getColor(context, R.color.gray_menu))
        )
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
                // 날짜 표시 텍스트
                Text(
                    text = addTodoViewModel.formatSelectedDateForCalendar(),
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
                    IconButton(onClick = { onMonthChanged(currentDate.monthValue - 1) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_left),
                            contentDescription = "Previous Month",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(id = R.color.ios_blue),
                        )
                    }
                    IconButton(onClick = { onMonthChanged(currentDate.monthValue + 1) }) {
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
        }
    }


}


//@Preview(showBackground = true)
//@Composable
//fun AddTodoScreenPreview() {
//    val previewViewModel = AddTodoViewModel()
//
//    // 날짜 미리 설정
//    previewViewModel.updateSelectedDate(LocalDate.now())
//
//    AddTodoScreen(previewViewModel)
//}
