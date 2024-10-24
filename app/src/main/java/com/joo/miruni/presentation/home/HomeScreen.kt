package com.joo.miruni.presentation.home

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.joo.miruni.R
import com.joo.miruni.presentation.addTask.addSchedule.AddScheduleActivity
import com.joo.miruni.presentation.addTask.addTodo.AddTodoActivity
import com.joo.miruni.presentation.detail.detailSchedule.DetailScheduleActivity
import com.joo.miruni.presentation.detail.detailTodo.DetailTodoActivity
import com.joo.miruni.presentation.widget.BasicDialog
import com.joo.miruni.presentation.widget.DialogMod
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    /*
    * Live Data
    * */
    val thingsToDoItems by homeViewModel.thingsTodoItems.observeAsState(emptyList())
    val scheduleItems by homeViewModel.scheduleItems.observeAsState(emptyList())
    val isTodoListLoading by homeViewModel.isTodoListLoading.observeAsState(false)
    val isScheduleListLoading by homeViewModel.isScheduleListLoading.observeAsState(false)
    val isCompletedViewChecked =
        homeViewModel.settingObserveCompleteVisibility.observeAsState(false)
    val selectDate by homeViewModel.selectDate.observeAsState(LocalDateTime.now())

    // FAB 메뉴
    var isAddMenuExpanded by remember { mutableStateOf(false) }

    // 한번 초기화가 되었는지 판단 변수
    var initialLoad by remember { mutableStateOf(true) }

    // 일정 페이지 상태
    val pageCount = (scheduleItems.size + 2) / 3
    val schedulePagerState = rememberPagerState(
        pageCount = {
            pageCount
        })

    // 무한 스크롤
    val todoBuffer = 3
    val lazyListState = rememberLazyListState()
    val reachesBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= lazyListState.layoutInfo.totalItemsCount - todoBuffer
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 왼쪽 버튼
                IconButton(
                    onClick = { homeViewModel.changeDate("<") }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_left),
                        contentDescription = "Date Icon"
                    )
                }

                // 중앙 텍스트 및 아이콘
                Row(
                    modifier = Modifier.weight(0.9f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = homeViewModel.formatSelectedDate(selectDate),
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

                // 오른쪽 버튼
                IconButton(onClick = {
                    homeViewModel.changeDate(">")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "Weather Icon"
                    )
                }
            }

            // 일정
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
                                    if (!isCompletedViewChecked.value && scheduleGroup[j].isComplete) {

                                    } else {
                                        ScheduleItem(context, scheduleGroup[j])
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
                        homeViewModel.loadMoreScheduleData()
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
                // 할 일 리스트
                items(thingsToDoItems.size) { index ->
                    ThingsToDoItem(
                        context,
                        homeViewModel,
                        thingsToDoItems[index],
                    )
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
                                        context.startActivity(intent)
                                        isAddMenuExpanded = false
                                        homeViewModel.collapseAllItems()
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
                                            Intent(context, AddScheduleActivity::class.java)
                                        context.startActivity(intent)
                                        isAddMenuExpanded = false
                                        homeViewModel.collapseAllItems()
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

    // 스크롤 이벤트를 통해 데이터 로드
    LaunchedEffect(reachesBottom) {
        if (reachesBottom && !isTodoListLoading && !initialLoad) {
            homeViewModel.loadMoreTodoItemsForAlarm()
        }
    }

    // 초기 로드 상태 업데이트
    LaunchedEffect(thingsToDoItems) {
        if (thingsToDoItems.isNotEmpty()) {
            initialLoad = false
        }
    }

}

// 일정 Item
@Composable
fun ScheduleItem(context: Context, schedule: Schedule) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val isDDayAnimation by rememberInfiniteTransition(label = "D-Day").animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "D-Day"
    )

    Card(
        modifier = Modifier
            .width((screenWidth / 3).dp)
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            )
            .clickable(
                indication = ripple(
                    bounded = true,
                    color = colorResource(R.color.ios_gray),
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                // 클릭 시 일정 상세보기
                val intent = Intent(
                    context,
                    DetailScheduleActivity::class.java
                ).apply {
                    putExtra(
                        "SCHEDULE_ID",
                        schedule.id
                    )
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!schedule.isComplete) {
                colorResource(id = R.color.gray_menu)
            } else {
                colorResource(id = R.color.ios_complete_gray) },
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // D-Day
                Text(
                    modifier = Modifier
                        .alpha(if (schedule.daysBefore == 0) isDDayAnimation else 1f),
                    text = if (schedule.daysBefore == 0) {
                        "D-DAY"
                    } else {
                        "D-${schedule.daysBefore}"
                    },
                    fontWeight = if (schedule.daysBefore == 0) {
                        FontWeight.Bold
                    } else {
                        FontWeight.SemiBold
                    },
                )

                // 일정 제목
                Text(
                    text = schedule.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// 할 일 Item
@Composable
fun ThingsToDoItem(context: Context, homeViewModel: HomeViewModel, thingsToDo: ThingsTodo) {

    // User Setting 값
    val isCompletedViewChecked =
        homeViewModel.settingObserveCompleteVisibility.observeAsState(false)

    var isOpenThingsTodoMenu by remember { mutableStateOf(false) }        // MenuIcon 터치 여부

    val isExpanded = homeViewModel.isItemExpanded(thingsToDo.id)                // 확장 여부
    val deletedItems by homeViewModel.deletedItems.observeAsState(emptySet())   // 삭제 여부
    var showDialog by remember { mutableStateOf(false) }                  // dialog 보여짐 여부
    var dialogMod by remember { mutableStateOf(DialogMod.TODO_DELETE) }              // dialog mod

    /*
    * 애니매이션
    * */
    val flashAnimation by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    AnimatedVisibility(
        visible = !deletedItems.contains(thingsToDo.id),
        enter = fadeIn(animationSpec = tween(durationMillis = 0)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {

        // 완료된 할일 필터링
        if (isCompletedViewChecked.value || !thingsToDo.isCompleted) {
            // 완료되지 않은 항목
            if (!thingsToDo.isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable(
                                indication = ripple(
                                    bounded = true,
                                    color = colorResource(R.color.ios_gray),
                                ),
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                // 클릭 시 확장
                                homeViewModel.toggleItemExpansion(thingsToDo.id)
                            },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.gray_menu),
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )

                                    .animateContentSize()
                            ) {
                                // 제목, 남은 기간, 더보기
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 제목
                                    Text(
                                        text = thingsToDo.title,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .padding(start = 2.dp)
                                    )

                                    // 마감일
                                    Text(
                                        text = homeViewModel.formatTimeRemaining(thingsToDo.deadline),
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    // 메뉴 아이콘
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                isOpenThingsTodoMenu = !isOpenThingsTodoMenu
                                            }
                                            .background(Color.Transparent)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_more),
                                            contentDescription = "See More",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.Black
                                        )

                                        // 메뉴
                                        DropdownMenu(
                                            expanded = isOpenThingsTodoMenu,
                                            onDismissRequest = { isOpenThingsTodoMenu = false },
                                            offset = DpOffset(x = 0.dp, y = (-24).dp),
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
                                                tonalElevation = 6.dp,
                                                shadowElevation = 6.dp,
                                            ) {

                                                // 메뉴 아이템
                                                Column {

                                                    // 상세보기 메뉴
                                                    Text(
                                                        text = "상세보기",
                                                        modifier = Modifier
                                                            .clickable(
                                                                indication = ripple(
                                                                    bounded = true,
                                                                    color = colorResource(R.color.ios_gray),
                                                                ),
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) {
                                                                // 상세보기 액티비티로 넘어가기
                                                                val intent = Intent(
                                                                    context,
                                                                    DetailTodoActivity::class.java
                                                                ).apply {
                                                                    putExtra(
                                                                        "TODO_ID",
                                                                        thingsToDo.id
                                                                    )
                                                                }
                                                                context.startActivity(intent)
                                                                isOpenThingsTodoMenu =
                                                                    !isOpenThingsTodoMenu
                                                                homeViewModel.collapseAllItems()
                                                            }
                                                            .padding(16.dp)
                                                            .defaultMinSize(60.dp)
                                                    )
                                                    HorizontalDivider(
                                                        thickness = 0.5.dp,
                                                        color = Color.Black.copy(alpha = 0.2f)
                                                    )

                                                    // 미루기 메뉴
                                                    Text(
                                                        text = "미루기",
                                                        modifier = Modifier
                                                            .clickable(
                                                                indication = ripple(
                                                                    bounded = true,
                                                                    color = colorResource(R.color.ios_gray),
                                                                ),
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) {
                                                                // 미루기 클릭 시
                                                                homeViewModel.delayTodoItem(
                                                                    thingsToDo
                                                                )
                                                                isOpenThingsTodoMenu =
                                                                    !isOpenThingsTodoMenu
                                                            }
                                                            .padding(16.dp)
                                                            .defaultMinSize(60.dp),
                                                        color = Color.Red
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // 설명칸 비었으면 무시
                                if (!thingsToDo.description.isNullOrEmpty()) {
                                    Text(
                                        text = thingsToDo.description,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 8.dp, bottom = 10.dp)
                                    )
                                }

                                // 추가 정보와 구분선
                                if (isExpanded) {
                                    HorizontalDivider(
                                        thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f),
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // 삭제 텍스트
                                        Column(
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .weight(1f)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    dialogMod = DialogMod.TODO_DELETE
                                                    showDialog = true
                                                }
                                                .padding(8.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "삭제",
                                                color = Color.Red,
                                                textAlign = TextAlign.Center,
                                            )
                                        }

                                        // 구분선
                                        Spacer(modifier = Modifier.width(12.dp))
                                        VerticalDivider(
                                            thickness = 1.dp,
                                            color = Color.Gray.copy(alpha = 0.5f),
                                            modifier = Modifier.height(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))

                                        // 완료 텍스트
                                        Column(
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .weight(1f)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    dialogMod = DialogMod.TODO_COMPLETE
                                                    showDialog = true
                                                }
                                                .padding(8.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "완료",
                                                color = colorResource(R.color.ios_blue),
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }

                                }

                                // 다이얼로그
                                BasicDialog(
                                    dialogType = dialogMod,
                                    showDialog = showDialog,
                                    onDismiss = {
                                        showDialog = false
                                        homeViewModel.collapseAllItems()
                                    },
                                    onCancel = {
                                        showDialog = false
                                        homeViewModel.collapseAllItems()
                                    },
                                    onConfirmed = {
                                        if (dialogMod == DialogMod.TODO_DELETE) {
                                            homeViewModel.deleteTaskItem(thingsToDo.id)
                                        } else if (dialogMod == DialogMod.TODO_COMPLETE) {
                                            homeViewModel.completeTask(thingsToDo.id)
                                        }
                                        homeViewModel.collapseAllItems()
                                    },
                                    title = thingsToDo.title,
                                )
                            }
                        }
                    }

                    // 알림 중요도 표시 등
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = when (homeViewModel.getColorForRemainingTime(thingsToDo.deadline)) {
                                    Importance.BLINK_RED -> Color.Red.copy(alpha = flashAnimation)
                                    Importance.RED -> Color.Red
                                    Importance.ORANGE -> colorResource(R.color.orange)
                                    Importance.YELLOW -> colorResource(R.color.yellow)
                                    Importance.GREEN -> Color.Green
                                    Importance.EMERGENCY -> Color.Red.copy(alpha = flashAnimation)
                                },
                                shape = RoundedCornerShape(90.dp)
                            )
                            .align(Alignment.TopStart)
                    )
                }
            }
            // 완료된 항목
            else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.ios_complete_gray),
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        // 클릭 시 확장
                                        homeViewModel.toggleItemExpansion(thingsToDo.id)
                                    }
                                    .animateContentSize()
                            ) {
                                // 제목, 남은 기간, 더보기
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 제목
                                    Text(
                                        text = thingsToDo.title,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .padding(start = 2.dp)
                                    )

                                    // 마감일
                                    Text(
                                        text = homeViewModel.formatTimeRemaining(thingsToDo.deadline),
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    // 메뉴 아이콘
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                isOpenThingsTodoMenu = !isOpenThingsTodoMenu
                                            }
                                            .background(Color.Transparent)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_more),
                                            contentDescription = "See More",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.Black
                                        )

                                        // 메뉴
                                        DropdownMenu(
                                            expanded = isOpenThingsTodoMenu,
                                            onDismissRequest = { isOpenThingsTodoMenu = false },
                                            offset = DpOffset(x = 0.dp, y = (-24).dp),
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
                                                tonalElevation = 6.dp,
                                                shadowElevation = 6.dp,
                                            ) {

                                                // 메뉴 아이템
                                                Column {

                                                    // 상세보기 메뉴
                                                    Text(
                                                        text = "상세보기",
                                                        modifier = Modifier
                                                            .clickable(
                                                                indication = ripple(
                                                                    bounded = true,
                                                                    color = colorResource(R.color.ios_gray),
                                                                ),
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) {

                                                                // 상세보기 액티비티로 넘어가기
                                                                val intent = Intent(
                                                                    context,
                                                                    DetailTodoActivity::class.java
                                                                ).apply {
                                                                    putExtra(
                                                                        "TODO_ID",
                                                                        thingsToDo.id
                                                                    )
                                                                }
                                                                context.startActivity(intent)
                                                                isOpenThingsTodoMenu =
                                                                    !isOpenThingsTodoMenu
                                                                homeViewModel.collapseAllItems()
                                                            }
                                                            .padding(16.dp)
                                                            .defaultMinSize(60.dp)
                                                    )
                                                    HorizontalDivider(
                                                        thickness = 0.5.dp,
                                                        color = Color.Black.copy(alpha = 0.2f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // 설명칸 비었으면 무시
                                if (!thingsToDo.description.isNullOrEmpty()) {
                                    Text(
                                        text = thingsToDo.description,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 8.dp, bottom = 10.dp)
                                    )
                                }

                                // 완료 날짜 표시
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp, end = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = "${homeViewModel.formatLocalDateTime(thingsToDo.completeDate)}에 완료함",
                                        fontSize = 10.sp
                                    )
                                }

                                // 추가 정보와 구분선
                                if (isExpanded) {
                                    HorizontalDivider(
                                        thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f),
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // 삭제 텍스트
                                        Column(
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .weight(1f)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    dialogMod = DialogMod.TODO_DELETE
                                                    showDialog = true
                                                }
                                                .padding(8.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "삭제",
                                                color = Color.Red,
                                                textAlign = TextAlign.Center,
                                            )
                                        }

                                        // 구분선
                                        Spacer(modifier = Modifier.width(12.dp))
                                        VerticalDivider(
                                            thickness = 1.dp,
                                            color = Color.Gray.copy(alpha = 0.5f),
                                            modifier = Modifier.height(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))

                                        // 완료 취소 텍스트
                                        Column(
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .weight(1f)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) {
                                                    dialogMod = DialogMod.TODO_CANCEL_COMPLETE
                                                    showDialog = true
                                                }
                                                .padding(8.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "완료 취소",
                                                color = colorResource(R.color.ios_blue),
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }

                                    // 다이얼로그
                                    BasicDialog(
                                        dialogType = dialogMod,
                                        showDialog = showDialog,
                                        onDismiss = {
                                            showDialog = false
                                            homeViewModel.collapseAllItems()
                                        },
                                        onCancel = {
                                            showDialog = false
                                            homeViewModel.collapseAllItems()
                                        },
                                        onConfirmed = {
                                            if (dialogMod == DialogMod.TODO_DELETE) {
                                                homeViewModel.deleteTaskItem(thingsToDo.id)
                                            } else if (dialogMod == DialogMod.TODO_CANCEL_COMPLETE) {
                                                homeViewModel.completeCancelTaskItem(thingsToDo.id)
                                            }
                                            homeViewModel.collapseAllItems()
                                        },
                                        title = thingsToDo.title,
                                    )
                                }
                            }
                        }
                    }
                    // 알림 중요도 표시 등
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.TopStart)
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


