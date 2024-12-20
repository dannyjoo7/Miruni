package com.joo.miruni.presentation.unlock

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.joo.miruni.presentation.ui.theme.MiruniTheme
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun UnlockScreen(
    unlockViewModel: UnlockViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    /*
    * Live Data
    * */

    val selectDate by unlockViewModel.selectDate.observeAsState(LocalDate.now())
    val thingsToDoItems by unlockViewModel.thingsTodoItems.observeAsState(emptyList())
    val scheduleItems by unlockViewModel.scheduleItems.observeAsState(emptyList())

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

    // 드래그 오프셋
    val dragOffset = remember { mutableFloatStateOf(0f) }

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
                modifier = Modifier.fillMaxSize()
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
                            text = unlockViewModel.formatSelectedDate(selectDate),
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

                // 일정
                if (scheduleItems.isNotEmpty()) {
                    HorizontalPager(
                        state = schedulePagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                    ) { page ->
                        val startIndex = page * 3
                        val endIndex = minOf(startIndex + 3, scheduleItems.size)

                        if (startIndex < scheduleItems.size) {
                            val scheduleGroup = scheduleItems.subList(startIndex, endIndex)

                            Column(
                                modifier = Modifier
                                    .height(60.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                for (i in scheduleGroup.indices step 3) {
                                    Row {
                                        for (j in i until minOf(i + 3, scheduleGroup.size)) {
                                            if (isCompletedViewChecked.value || !scheduleGroup[j].isComplete) {
                                                ScheduleItem(
                                                    context = context,
                                                    schedule = scheduleGroup[j],
                                                    isClickable = false
                                                )
                                            }
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
                    .align(Alignment.BottomCenter)
                    .height(200.dp)
                    .background(Color.Transparent)
                    .offset { IntOffset(dragOffset.floatValue.roundToInt(), 0) }
                    .draggable(
                        state = rememberDraggableState { delta ->
                            dragOffset.floatValue += delta
                        },
                        orientation = Orientation.Horizontal,
                        onDragStarted = {
                        },
                        onDragStopped = {
                            if (dragOffset.floatValue > 200) {
                                (context as? Activity)?.finish()
                            }
                            dragOffset.floatValue = 0f
                        }
                    )
            ) {
                Text(
                    text = "왼쪽으로 밀어서 잠금 해제",
                    color = colorResource(R.color.ios_gray),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 16.sp
                )
            }
        }
    }
}