package com.joo.miruni.presentation.addTask.addSchedule

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
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.joo.miruni.presentation.widget.AlarmDisplayDatePicker
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    addScheduleViewModel: AddScheduleViewModel = hiltViewModel(),
) {

    // 현재 컨택스트
    val context = LocalContext.current

    // 키보드 컨트롤러
    val keyboardController = LocalSoftwareKeyboardController.current

    // 스크롤 상태
    val scrollState = rememberScrollState()

    /*
    * Live Data
    *  */
    val titleText by addScheduleViewModel.titleText.observeAsState("")
    val descriptionText by addScheduleViewModel.descriptionText.observeAsState("")

    val showStartDatePicker by addScheduleViewModel.showStartDatePicker.observeAsState(false)
    val showAlarmDisplayStartDatePicker by addScheduleViewModel.showAlarmDisplayStartDatePicker.observeAsState(
        true
    )

    val selectedStartDate by addScheduleViewModel.selectedStartDate.observeAsState()
    val selectedEndDate by addScheduleViewModel.selectedEndDate.observeAsState()
    val selectedAlarmDisplayDate by addScheduleViewModel.selectedAlarmDisplayDate.observeAsState()

    val isTodoTextEmpty by addScheduleViewModel.isTitleTextEmpty.observeAsState(false)
    val isScheduleAdded by addScheduleViewModel.isScheduleAdded.observeAsState(false)

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


    // Schedule 추가 성공 시 해당 액티비티 종료
    LaunchedEffect(isScheduleAdded) {
        if (isScheduleAdded) {
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
                            text = "일정",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "추가",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    addScheduleViewModel.addScheduleItem()
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
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 일정 입력창
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                            .offset(x = with(LocalDensity.current) { shakeOffset.toDp() })
                    ) {
                        Text(
                            text = "일정",
                            modifier = Modifier
                                .padding(end = 34.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        TextField(
                            value = titleText,
                            onValueChange = {
                                addScheduleViewModel.updateTitleText(it)
                            },
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "제목",
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
                                addScheduleViewModel.updateDescriptionText(it)
                            },
                            singleLine = false,
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
                                .heightIn(min = 56.dp),
                        )
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                }

                // 시작일 및 종료일 설정창
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
                            text = "기간",
                            modifier = Modifier
                                .weight(0.1f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )


                        // 시작일 및 종료일
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .clickable {
                                    addScheduleViewModel.clickedStartDateRangePickerBtn()
                                    keyboardController?.hide()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 4.dp,
                                    vertical = 4.dp,
                                ),
                                text = addScheduleViewModel.formatSelectedDateForCalendar(
                                    selectedStartDate
                                ),
                                color = colorResource(id = R.color.ios_blue),
                                fontSize = 18.sp,
                            )
                            Image(
                                modifier = Modifier
                                    .size(8.dp),
                                painter = painterResource(id = R.drawable.ic_vertical_dot),
                                contentDescription = "complete",
                                colorFilter = ColorFilter.tint(colorResource(R.color.ios_blue)),
                            )
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 4.dp,
                                    vertical = 4.dp,
                                ),
                                text = addScheduleViewModel.formatSelectedDateForCalendar(
                                    selectedEndDate
                                ),
                                color = colorResource(id = R.color.ios_blue),
                                fontSize = 18.sp,
                            )
                        }


                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)

                    /*
                    * 날짜 선택기
                    * */
                    Box {
                        // 날짜 선택기
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showStartDatePicker,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            DateRangePicker(
                                context = context,
                                selectedStartDate = selectedStartDate,
                                selectedEndDate = selectedEndDate,
                                onDateSelected = { startDate, endDate ->
                                    if (startDate == null && endDate == null) {
                                        addScheduleViewModel.initSelectedDate()
                                    }
                                    if (startDate != null) {
                                        addScheduleViewModel.selectStartDate(startDate)
                                    }
                                    if (endDate != null) {
                                        addScheduleViewModel.selectEndDate(endDate)
                                    }
                                },
                            )
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
                                        addScheduleViewModel.clickedAlarmDisplayStartDateText()
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
                                        addScheduleViewModel.updateSelectedAlarmDisplayDate(
                                            newAmount,
                                            null
                                        )
                                    },
                                    onDurationUnitChanged = { newUnit ->
                                        addScheduleViewModel.updateSelectedAlarmDisplayDate(
                                            null,
                                            newUnit
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        addScheduleViewModel.clickedAlarmDisplayStartDateText()
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
fun DateRangePicker(
    context: Context,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    onDateSelected: (LocalDate?, LocalDate?) -> Unit,
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
                            val isStartDateSelected = selectedStartDate?.dayOfMonth == dayOfMonth && selectedStartDate.monthValue == month
                            val isEndDateSelected = selectedEndDate?.dayOfMonth == dayOfMonth && selectedEndDate.monthValue == month

                            // 선택 범위 안에 있는가
                            val isInRange = selectedStartDate != null && selectedEndDate != null &&
                                    !currentDay.isBefore(selectedStartDate) && !currentDay.isAfter(selectedEndDate)

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
