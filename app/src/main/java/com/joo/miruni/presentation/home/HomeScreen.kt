package com.joo.miruni.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joo.miruni.R
import com.joo.miruni.presentation.home.model.ThingsToDo

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val thingsToDoItems = homeViewModel.thingsToDoItems
    val lazyListState = rememberLazyListState()
    val isLoading = false

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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

        // 스크롤 가능한 리스트
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            // 아이템 리스트
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
