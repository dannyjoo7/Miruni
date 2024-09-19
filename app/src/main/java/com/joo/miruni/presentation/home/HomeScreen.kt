package com.joo.miruni.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joo.miruni.R
import com.joo.miruni.presentation.home.model.Schedule
import com.joo.miruni.presentation.home.model.ThingsToDo

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val thingsToDoItems by homeViewModel.thingsToDoItems.observeAsState(emptyList())
    val scheduleItems by homeViewModel.scheduleItems.observeAsState(emptyList())

    // 무한 스크롤
    val lazyListState = rememberLazyListState()
    val isLoading = false

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
                IconButton(onClick = { /* Handle left button click */ }) {
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
                        text = "오늘",
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
                IconButton(onClick = { /* Handle right button click */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "Weather Icon"
                    )
                }
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                // 일정 리스트
                items(scheduleItems.size) { index ->
                    ScheduleItem(scheduleItems[index])
                }

                // 할 일 리스트
                items(thingsToDoItems.size) { index ->
                    ThingsToDoItem(thingsToDoItems[index])
                }

                // 로딩 인디케이터
                if (isLoading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }


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
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(
                    modifier = Modifier.size(68.dp),
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    contentDescription = "Add Item",
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
                    color = Color(ContextCompat.getColor(context, R.color.gray_menu)),
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
                                .clickable {
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
                                .clickable {
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
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                // 마지막 아이템에 도달했을 때 loadMoreData() 호출
                if (lastVisibleItemIndex == totalItems - 1 && !isLoading) {
                    homeViewModel.loadMoreData()
                }
            }
    }
}


@Composable
fun ScheduleItem(schedule: Schedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            ),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Text(
                text = schedule.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun ThingsToDoItem(thingsToDo: ThingsToDo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        // 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 2.dp,
                    top = 2.dp,
                ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 0.dp,
                            bottom = 8.dp,
                        ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 제목
                        Text(
                            text = thingsToDo.title,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.9f)
                        )
                        // 마감일
                        Text(
                            text = thingsToDo.deadline,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 4.dp) // 간격 조정
                        )
                        IconButton(
                            onClick = { /* Handle more actions */ },
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_more),
                                modifier = Modifier.size(12.dp), // 아이콘 크기 설정
                                contentDescription = "See More"
                            )
                        }
                    }

                    // 설명은 한 줄로 제한하고 넘어가면 ... 표시
                    Text(
                        text = thingsToDo.description ?: "",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // 넘칠 때 ...으로 표시
                    )
                }
            }
        }

        // 알람 표시등: 카드의 왼쪽 위에 위치
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(Color.Red, shape = RoundedCornerShape(90.dp))
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 4.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val previewViewModel = HomeViewModel()

    HomeScreen(navController, previewViewModel)
}
