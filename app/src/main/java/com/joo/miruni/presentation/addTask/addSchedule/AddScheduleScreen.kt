package com.joo.miruni.presentation.addTask.addSchedule

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joo.miruni.R
import com.joo.miruni.presentation.widget.DateRangePicker


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

    val showDateRangePicker by addScheduleViewModel.showDateRangePicker.observeAsState(false)
    val selectedStartDate by addScheduleViewModel.selectedStartDate.observeAsState()
    val selectedEndDate by addScheduleViewModel.selectedEndDate.observeAsState()

    val isTitleTextEmpty by addScheduleViewModel.isTitleTextEmpty.observeAsState(false)
    val isDateEmpty by addScheduleViewModel.isDateEmpty.observeAsState(false)
    val isScheduleAdded by addScheduleViewModel.isScheduleAdded.observeAsState(false)

    /*
    * UI
    * */

    val titleTextColor = if (isTitleTextEmpty) Color.Red else colorResource(R.color.ios_gray)
    val dateTextColor =
        if (isDateEmpty) Color.Red else colorResource(R.color.ios_blue)

    /*
    * 애니매이션
    * */

    val titleShakeOffset = remember { Animatable(0f) }
    val dateShakeOffset = remember { Animatable(0f) }

    // keyFrames 단위로 흔들기 애니매이션
    val shakeKeyframes: AnimationSpec<Float> = keyframes {
        durationMillis = 400
        val easing = FastOutLinearInEasing

        for (i in 1..8) {
            val x = when (i % 3) {
                0 -> 4f
                1 -> -4f
                else -> 0f
            }
            x at durationMillis / 10 * i using easing
        }
    }

    // 비었을 시 애니메이션 실행
    LaunchedEffect(isTitleTextEmpty, isDateEmpty) {
        if (isTitleTextEmpty) {
            titleShakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = shakeKeyframes
            )
            addScheduleViewModel.finishAnimation()
        }

        if (isDateEmpty) {
            dateShakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = shakeKeyframes
            )
            addScheduleViewModel.finishAnimation()
        }
    }


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
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
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
                            .offset {
                                IntOffset(
                                    x = titleShakeOffset.value
                                        .toDp()
                                        .toPx()
                                        .toInt(),
                                    y = 0
                                )
                            }
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
                                    color = titleTextColor
                                )

                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = colorResource(R.color.ios_blue),
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
                                cursorColor = colorResource(R.color.ios_blue),
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
                            .offset {
                                IntOffset(
                                    x = dateShakeOffset.value
                                        .toDp()
                                        .toPx()
                                        .toInt(),
                                    y = 0
                                )
                            },
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
                                    addScheduleViewModel.clickedDateRangePickerBtn()
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
                                color = dateTextColor,
                                fontSize = 18.sp,
                            )
                            Image(
                                modifier = Modifier
                                    .size(8.dp),
                                painter = painterResource(id = R.drawable.ic_vertical_dot),
                                contentDescription = "complete",
                                colorFilter = ColorFilter.tint(dateTextColor),
                            )
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 4.dp,
                                    vertical = 4.dp,
                                ),
                                text = addScheduleViewModel.formatSelectedDateForCalendar(
                                    selectedEndDate
                                ),
                                color = dateTextColor,
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
                            visible = showDateRangePicker,
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
                                onDismiss = {
                                    addScheduleViewModel.clickedDateRangePickerBtn()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
