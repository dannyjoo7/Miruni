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
import com.joo.miruni.presentation.addTodo.AddTodoActivity
import com.joo.miruni.presentation.detailPage.ModifyActivity
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val buffer = 3 // 스크롤이 마지막 n개 항목에 도달하면 더 로드(n >= 1)

    /*
    * Live Data
    * */
    val thingsToDoItems by homeViewModel.thingsTodoItems.observeAsState(emptyList())
    val scheduleItems by homeViewModel.scheduleItems.observeAsState(emptyList())
    val isLoading by homeViewModel.isLoading.observeAsState(false)
    val selectDate by homeViewModel.selectDate.observeAsState(LocalDateTime.now())

    var initialLoad by remember { mutableStateOf(true) }

    // 무한 스크롤
    val lazyListState = rememberLazyListState()
    val reachesBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= lazyListState.layoutInfo.totalItemsCount - buffer
        }
    }

    // FAB 메뉴
    var isAddMenuExpanded by remember { mutableStateOf(false) }

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

            // LazyColumn
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                // 일정 리스트
                items(scheduleItems.size) { index ->
                    ScheduleItem(
                        homeViewModel,
                        scheduleItems[index],
                    )
                }

                // 할 일 리스트
                items(thingsToDoItems.size) { index ->
                    ThingsToDoItem(
                        context,
                        homeViewModel,
                        thingsToDoItems[index],
                    )
                }

                // 로딩 인디케이터
                if (isLoading) {
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
                containerColor = Color.Gray,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    modifier = Modifier.size(68.dp),
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    contentDescription = "Add Item",
                    tint = Color.White,
                )
            }

            // 메뉴
            DropdownMenu(
                expanded = isAddMenuExpanded,
                onDismissRequest = { isAddMenuExpanded = false },
                offset = DpOffset(x = (-48).dp, y = (0).dp),
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
                    tonalElevation = 12.dp,
                    shadowElevation = 12.dp,
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
                                    isAddMenuExpanded = false
                                    val intent = Intent(context, AddTodoActivity::class.java)
                                    context.startActivity(intent)
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

    // 스크롤 이벤트를 통해 데이터 로드
    LaunchedEffect(reachesBottom) {
        if (reachesBottom && !isLoading && !initialLoad) {
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
fun ScheduleItem(homeViewModel: HomeViewModel, schedule: Schedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            ),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.gray_menu),
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // D-Day
                Text(
                    text = "D-${schedule.reminderDaysBefore}",
                    fontWeight = FontWeight.Bold,
                )

                // 일정 제목
                Text(
                    text = schedule.title,
                )
            }
        }
    }
}

// 할 일 Item
@Composable
fun ThingsToDoItem(context: Context, homeViewModel: HomeViewModel, thingsToDo: ThingsTodo) {

    var isOpenThingsTodoMenu by remember { mutableStateOf(false) }        // MenuIcon 터치 여부

    val isExpanded = homeViewModel.isItemExpanded(thingsToDo.id)                // 확장 여부
    val deletedItems by homeViewModel.deletedItems.observeAsState(emptySet())   // 삭제 여부

    /*
    * 애니매이션 */
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
        if (!thingsToDo.isCompleted) {
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
                                    .padding(vertical = 6.dp, horizontal = 4.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        // 클릭 시 확장
                                        homeViewModel.toggleItemExpansion(thingsToDo.id)
                                    },
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
                                        .weight(0.1f)
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

                                                // 수정 메뉴
                                                Text(
                                                    text = "수정",
                                                    modifier = Modifier
                                                        .clickable(
                                                            indication = ripple(
                                                                bounded = true,
                                                                color = colorResource(R.color.ios_gray),
                                                            ),
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            // 수정 액티비티로 넘어가기
                                                            val intent = Intent(
                                                                context,
                                                                ModifyActivity::class.java
                                                            ).apply {
                                                                putExtra("TODO_ID", thingsToDo.id)
                                                            }
                                                            context.startActivity(intent)
                                                            isOpenThingsTodoMenu =
                                                                !isOpenThingsTodoMenu
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
                                                            homeViewModel.delayTodoItem(thingsToDo)
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
                                                homeViewModel.deleteTaskItem(thingsToDo.id)
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
                                                homeViewModel.completeTask(thingsToDo.id)
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
    }
}