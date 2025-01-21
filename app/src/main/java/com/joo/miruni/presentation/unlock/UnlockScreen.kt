package com.joo.miruni.presentation.unlock

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joo.miruni.R
import com.joo.miruni.presentation.home.ScheduleItem
import com.joo.miruni.presentation.home.ThingsToDoItem
import com.joo.miruni.presentation.main.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun UnlockScreen(
    unlockViewModel: UnlockViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    /*
    * Live Data
    * */

    val thingsToDoItems by unlockViewModel.thingsTodoItems.observeAsState(emptyList())
    val scheduleItems by unlockViewModel.scheduleItems.observeAsState(emptyList())
    val currentDateTime by unlockViewModel.curDateTime.observeAsState()

    val isTodoListLoading by unlockViewModel.isTodoListLoading.observeAsState(false)
    val isScheduleListLoading by unlockViewModel.isScheduleListLoading.observeAsState(false)
    val isCompletedViewChecked =
        unlockViewModel.settingObserveCompleteVisibility.observeAsState(false)

    // 일정 페이지 상태
    val pageCount = (scheduleItems.size + 2) / 3
    val schedulePagerState = rememberPagerState(
        pageCount = {
            pageCount
        })

    // 무한 스크롤
    val lazyListState = rememberLazyListState()

    // 드래그
    val dragOffset = remember { mutableFloatStateOf(0f) }

    // 화면 너비 가져오기
    val screenWidth = LocalConfiguration.current.screenWidthDp * LocalDensity.current.density
    val halfScreenWidth = screenWidth / 2

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 중앙 텍스트 및 아이콘
                    Row(
                        modifier = Modifier.weight(0.9f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = unlockViewModel.formatSelectedDate(
                                currentDateTime?.toLocalDate() ?: LocalDate.now()
                            ),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_weather),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // 시간
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentDateTime?.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                            ?: "알 수 없음",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // 일정
                if (scheduleItems.isNotEmpty()) {
                    HorizontalPager(
                        state = schedulePagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                    ) { page ->
                        // 완료된 항목 필터 리스트
                        val filteredScheduleItems = scheduleItems.filter {
                            isCompletedViewChecked.value || !it.isComplete
                        }

                        val startIndex = page * 3
                        val endIndex = minOf(startIndex + 3, filteredScheduleItems.size)

                        if (startIndex < filteredScheduleItems.size) {
                            val scheduleGroup = filteredScheduleItems.subList(startIndex, endIndex)

                            Column(
                                modifier = Modifier
                                    .height(60.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                for (i in scheduleGroup.indices step 3) {
                                    Row {
                                        for (j in i until minOf(i + 3, scheduleGroup.size)) {
                                            ScheduleItem(
                                                context = context,
                                                schedule = scheduleGroup[j],
                                                onLongClicked = { },
                                                isClickable = false
                                            )
                                        }
                                        // 로딩바
                                        if (isScheduleListLoading && page == pageCount - 1) {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .padding(16.dp),
                                                color = colorResource(R.color.ios_gray)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 페이지가 끝에 도달했을 때 추가 데이터 로드
                        LaunchedEffect(schedulePagerState.currentPage) {
                            if (page == pageCount - 1) {
                                unlockViewModel.loadMoreScheduleData()
                            }
                        }
                    }
                }

                // 할 일
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    if (thingsToDoItems.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "할 일이 없습니다",
                                    color = colorResource(R.color.ios_gray),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    } else {
                        items(thingsToDoItems.size) { index ->
                            val thingsToDo = thingsToDoItems[index]

                            if (isCompletedViewChecked.value || !thingsToDo.isCompleted) {
                                ThingsToDoItem(
                                    context = context,
                                    thingsToDo = thingsToDo,
                                    onClicked = { },
                                    onClickedShowDetail = { },
                                    onClickedDelay = { },
                                    onDialogConfirmed = { },
                                    onLongClicked = { },
                                    isDelete = false,
                                    isExpanded = false,
                                    isClickable = false,
                                )
                            }
                        }
                    }

                    // 로딩 인디케이터
                    if (isTodoListLoading) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(16.dp),
                                    color = colorResource(R.color.ios_gray)
                                )
                            }
                        }
                    }
                }
            }

            // 잠금 해제 위젯
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.white))
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "앱 열기",
                        color = colorResource(R.color.ios_gray_calander_font),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "잠금 해제",
                        color = colorResource(R.color.ios_gray_calander_font),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                // 드래그 가능한 버튼
                Card(
                    modifier = Modifier
                        .offset { IntOffset(dragOffset.floatValue.roundToInt(), 0) }
                        .size(width = 72.dp, height = 72.dp)
                        .draggable(
                            state = rememberDraggableState { delta ->
                                dragOffset.floatValue += delta
                            },
                            orientation = Orientation.Horizontal,
                            onDragStarted = {},
                            onDragStopped = {
                                when {
                                    dragOffset.floatValue > halfScreenWidth - 150 -> {
                                        // 오른쪽으로 스와이프
                                        (context as? Activity)?.finish()
                                    }

                                    dragOffset.floatValue < -halfScreenWidth + 150 -> {
                                        // 왼쪽으로 스와이프
                                        val intent =
                                            Intent(context, MainActivity::class.java).apply {
                                                flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            }
                                        context.startActivity(intent)
                                        (context as? Activity)?.finish()
                                    }
                                }
                                // 드래그 끝난 후 초기화
                                dragOffset.floatValue = 0f
                            }
                        )
                        .clickable(
                            indication = ripple(
                                bounded = true,
                                color = colorResource(R.color.ios_gray),
                            ),
                            interactionSource = remember { MutableInteractionSource() }
                        ) {},
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(colorResource(R.color.ios_light_gray))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_swap_arrows),
                            contentDescription = "Unlock or Start App Button",
                        )
                    }
                }
            }


        }
    }
}