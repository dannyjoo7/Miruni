package com.joo.miruni.presentation.calendar

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.joo.miruni.R
import com.joo.miruni.data.entities.TaskType
import com.joo.miruni.presentation.addTask.addSchedule.AddScheduleActivity
import com.joo.miruni.presentation.addTask.addTodo.AddTodoActivity
import com.joo.miruni.presentation.detail.detailSchedule.DetailScheduleActivity
import com.joo.miruni.presentation.detail.detailTodo.DetailTodoActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    /*
    * Live Date
    * */
    val selectDate by calendarViewModel.selectedDate.observeAsState()
    val selectedDateTaskList by calendarViewModel.selectedDateTaskList.observeAsState(emptyList())
    val taskExistList by calendarViewModel.taskExistList.observeAsState(emptyList())

    // FAB 메뉴
    var isAddMenuExpanded by remember { mutableStateOf(false) }

    // 폴딩 여부
    val isFolded = LocalConfiguration.current.screenWidthDp < 600

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 펼쳐짐
        if (!isFolded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                ) {
                    DatePickerForTask(
                        context = context,
                        selectedDate = selectDate,
                        taskExistList = taskExistList,
                        onDateSelected = { date -> calendarViewModel.selectDate(date) },
                        onMonthChange = { date -> calendarViewModel.monthChange(date) },
                    )
                }

                VerticalDivider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 태스크 리스트 아이템
                    items(selectedDateTaskList.size) { index ->
                        val task = selectedDateTaskList[index]
                        TaskWidget(task)
                    }
                }
            }
        }
        // 닫힘
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        DatePickerForTask(
                            context = context,
                            selectedDate = selectDate,
                            taskExistList = taskExistList,
                            onDateSelected = { date -> calendarViewModel.selectDate(date) },
                            onMonthChange = { date -> calendarViewModel.monthChange(date) },
                        )
                    }
                    HorizontalDivider(
                        color = colorResource(id = R.color.ios_gray),
                        thickness = 0.5.dp
                    )
                }

                // 태스크 리스트 아이템
                items(selectedDateTaskList.size) { index ->
                    val task = selectedDateTaskList[index]
                    TaskWidget(task)
                }
            }
        }

        // 플로팅 버튼 + 메뉴
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // FAB
            FloatingActionButton(
                onClick = { isAddMenuExpanded = !isAddMenuExpanded },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
            ) {
                Icon(
                    modifier = Modifier.size(68.dp),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Item",
                    tint = Color.Black,
                )

                // 메뉴
                DropdownMenu(
                    expanded = isAddMenuExpanded,
                    onDismissRequest = { isAddMenuExpanded = false },
                    offset = DpOffset(x = (-72).dp, y = (48).dp),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colorResource(R.color.gray_menu),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        tonalElevation = 4.dp,
                        shadowElevation = 4.dp,
                    ) {
                        Column {
                            Text(
                                text = "할 일",
                                modifier = Modifier
                                    .clickable(
                                        indication = ripple(
                                            bounded = true,
                                            color = colorResource(R.color.ios_gray),
                                        ),
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        // 할 일 추가
                                        val intent = Intent(context, AddTodoActivity::class.java)
                                            .apply {
                                                putExtra(
                                                    "SELECT_DATE",
                                                    selectDate.toString()
                                                )
                                            }
                                        context.startActivity(intent)
                                        isAddMenuExpanded = false
                                    }
                                    .padding(16.dp)
                                    .defaultMinSize(60.dp)
                            )
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color.Black.copy(alpha = 0.2f)
                            )
                            Text(
                                text = "일정",
                                modifier = Modifier
                                    .clickable(
                                        indication = ripple(
                                            bounded = true,
                                            color = colorResource(R.color.ios_gray),
                                        ),
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        // 일정 추가
                                        val intent =
                                            Intent(context, AddScheduleActivity::class.java).apply {
                                                putExtra(
                                                    "SELECT_DATE",
                                                    selectDate.toString()
                                                )
                                            }
                                        context.startActivity(intent)
                                        isAddMenuExpanded = false
                                    }
                                    .padding(16.dp)
                                    .defaultMinSize(60.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DatePickerForTask(
    context: Context,
    selectedDate: LocalDate?,
    taskExistList: List<Boolean>,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (LocalDate) -> Unit,
) {
    var currentDate by remember { mutableStateOf(selectedDate ?: LocalDate.now()) }
    val today = LocalDate.now()

    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    // 달력 렌더링
    val renderCalendar: @Composable () -> Unit = {
        val year = currentDate.year
        val month = currentDate.monthValue
        val daysInMonth = currentDate.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(year, month, 1).dayOfWeek.value % 7

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
                            val hasTask = if (dayOfMonth - 1 in taskExistList.indices) {
                                taskExistList[dayOfMonth - 1]
                            } else {
                                false
                            }

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
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }

                                    ) {
                                        onDateSelected(LocalDate.of(year, month, dayOfMonth))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = dayOfMonth.toString(),
                                        color = when {
                                            isSelected -> colorResource(id = R.color.ios_blue)
                                            isToday -> colorResource(id = R.color.ios_blue)
                                            hasTask -> colorResource(id = R.color.ios_blue)
                                            else -> Color.Black
                                        },
                                        fontSize = when {
                                            isSelected -> 20.sp
                                            else -> 18.sp
                                        },
                                        fontWeight = when {
                                            hasTask -> FontWeight.Bold
                                            else -> FontWeight.Normal
                                        }
                                    )
                                }
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
            .wrapContentSize(),
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
                // 날짜 표시 텍스트
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
                    // < 버튼
                    IconButton(onClick = {
                        currentDate = currentDate.minusMonths(1)
                        currentDate =
                            if (currentDate.year == today.year && currentDate.month == today.month) {
                                today
                            } else {
                                currentDate.withDayOfMonth(1)
                            }
                        onMonthChange(currentDate)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_left),
                            contentDescription = "Previous Month",
                            modifier = Modifier.size(20.dp),
                            tint = colorResource(id = R.color.ios_blue),
                        )
                    }

                    // > 버튼
                    IconButton(onClick = {
                        currentDate = currentDate.plusMonths(1)
                        currentDate =
                            if (currentDate.year == today.year && currentDate.month == today.month) {
                                today
                            } else {
                                currentDate.withDayOfMonth(1)
                            }
                        onMonthChange(currentDate)
                    }) {
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

@Composable
fun TaskWidget(taskItem: TaskItem) {

    val context = LocalContext.current

    // 날짜 포멧
    fun formatDate(date: LocalDate?): String {
        val currentYear = LocalDate.now().year

        val formatterSameYear = DateTimeFormatter.ofPattern("M월 d일")
        val formatterDifferentYear = DateTimeFormatter.ofPattern("yyyy년 M월 d일")

        return if (date?.year == currentYear) {
            date.format(formatterSameYear)
        } else {
            date?.format(formatterDifferentYear) ?: "알 수 없음"
        }
    }

    // 날짜 및 시간 포멧
    fun formatDateTime(dateTime: LocalDateTime?): String {
        val currentYear = LocalDate.now().year

        val formatterSameYear = DateTimeFormatter.ofPattern("M월 d일 HH시 mm분")
        val formatterDifferentYear = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH시 mm분")

        return if (dateTime?.year == currentYear) {
            dateTime.format(formatterSameYear)
        } else {
            dateTime?.format(formatterDifferentYear) ?: "알 수 없음"
        }
    }


    Box(
        modifier = Modifier
            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable(
                indication = ripple(
                    bounded = true,
                    color = colorResource(R.color.ios_gray),
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                val intent = when (taskItem.type) {
                    TaskType.SCHEDULE -> {
                        Intent(
                            context,
                            DetailScheduleActivity::class.java
                        ).apply {
                            putExtra(
                                "SCHEDULE_ID",
                                taskItem.id
                            )
                        }
                    }

                    TaskType.TODO -> {
                        Intent(
                            context,
                            DetailTodoActivity::class.java
                        ).apply {
                            putExtra(
                                "TODO_ID",
                                taskItem.id
                            )
                        }
                    }
                }
                context.startActivity(intent)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // 제목, 항목
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 제목
                Text(
                    text = taskItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                // 항목 및 완료 여부
                when (taskItem.type) {
                    TaskType.SCHEDULE -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "일정",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            if (taskItem.isComplete) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "complete",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                color = Color.Green,
                                                shape = RoundedCornerShape(90.dp)
                                            )
                                            .padding(2.dp),
                                        colorFilter = ColorFilter.tint(Color.White),
                                    )
                                }
                            }
                        }
                    }

                    TaskType.TODO -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "할 일",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            if (taskItem.isComplete) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "complete",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                color = Color.Green,
                                                shape = RoundedCornerShape(90.dp)
                                            )
                                            .padding(2.dp),
                                        colorFilter = ColorFilter.tint(Color.White),
                                    )
                                }
                            }
                        }
                    }
                }

            }
            Text(
                text = taskItem.details ?: "",
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                when (taskItem.type) {
                    TaskType.SCHEDULE -> {
                        Text(
                            text = "시작일 : ${formatDate(taskItem.startDate)} / 종료일 : ${
                                formatDate(
                                    taskItem.endDate
                                )
                            }",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 10.sp
                        )
                    }

                    TaskType.TODO -> {
                        Text(
                            text = "마감일 : ${formatDateTime(taskItem.deadline)}",
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 10.sp
                        )
                    }
                }

            }

        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.Black.copy(alpha = 0.2f))
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskWidget() {
    val sampleTodoItem = TaskItem(
        id = 1,
        deadline = LocalDateTime.now(),
        startDate = null,
        endDate = null,
        title = "할 일",
        details = "할 일 세부 사항",
        isComplete = true,
        completeDate = null,
        type = TaskType.TODO
    )

    val sampleScheduleItem = TaskItem(
        id = 1,
        deadline = null,
        startDate = LocalDate.now(),
        endDate = LocalDate.now(),
        title = "일정",
        details = "일정 세부 사항",
        isComplete = true,
        completeDate = null,
        type = TaskType.SCHEDULE
    )

    TaskWidget(taskItem = sampleScheduleItem)
//    TaskWidget(taskItem = sampleScheduleItem)
}
