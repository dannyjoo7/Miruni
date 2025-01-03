package com.joo.miruni.presentation.overdue

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joo.miruni.R
import com.joo.miruni.presentation.detail.detailTodo.DetailTodoActivity
import com.joo.miruni.presentation.home.ThingsToDoItem
import com.joo.miruni.presentation.widget.BasicDialog
import com.joo.miruni.presentation.widget.DialogMod

@Composable
fun OverdueScreen(
    overdueViewModel: OverdueViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    /*
    * LiveData
    * */
    val overdueTodoItems by overdueViewModel.overdueTodoItems.observeAsState(emptyList())
    val isOverdueTodoListLoading by overdueViewModel.isOverdueTodoListLoading.observeAsState(false)
    val isDelayAllTodoLoading by overdueViewModel.isDelayAllTodoLoading.observeAsState(false)
    val deletedItems by overdueViewModel.deletedItems.observeAsState()

    // 한번 초기화가 되었는지 판단 변수
    var initialLoad by remember { mutableStateOf(true) }

    // 무한 스크롤
    val lazyListState = rememberLazyListState()

    // 다이얼로그 show 여부
    var isShowDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        overdueViewModel.loadInitOverdueTasks()
    }

    // 초기 로드 상태 업데이트
    LaunchedEffect(overdueTodoItems) {
        if (overdueTodoItems.isNotEmpty()) {
            initialLoad = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "기한이 지난 할 일",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // 기한이 지난 할 일
            if (overdueTodoItems.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "기한이 지난\n할 일이 없습니다",
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        color = colorResource(R.color.ios_gray),
                    )
                }
            }
            // 모두 삭제 진행 상태 중일시
            else if (isDelayAllTodoLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(R.color.ios_divider)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = colorResource(R.color.ios_gray)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 할 일
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 8.dp)
                    ) {
                        // 할 일 리스트
                        items(overdueTodoItems.size) { index ->
                            val thingsToDo = overdueTodoItems[index]

                            val isDelete = deletedItems?.contains(thingsToDo.id) ?: false
                            val isExpanded =
                                overdueViewModel.expandedItems.value.contains(thingsToDo.id)

                            ThingsToDoItem(
                                context = context,
                                thingsToDo = thingsToDo,
                                onClicked = {
                                    overdueViewModel.toggleItemExpansion(thingsToDo.id)
                                },
                                onClickedShowDetail = {
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
                                    overdueViewModel.collapseAllItems()
                                },
                                onClickedDelay = {
                                    overdueViewModel.delayTodoItem(thingsToDo)
                                },
                                onDialogConfirmed = { dialogMod ->
                                    when (dialogMod) {
                                        DialogMod.TODO_COMPLETE -> {
                                            overdueViewModel.completeTask(thingsToDo.id)
                                            overdueViewModel.collapseAllItems()
                                        }

                                        DialogMod.TODO_DELETE -> {
                                            overdueViewModel.deleteTaskItem(thingsToDo.id)
                                            overdueViewModel.collapseAllItems()
                                        }

                                        else -> {
                                            overdueViewModel.collapseAllItems()
                                        }
                                    }
                                },
                                onLongClicked = {

                                },
                                isDelete = isDelete,
                                isExpanded = isExpanded,
                            )
                        }

                        // 로딩 인디케이터
                        if (isOverdueTodoListLoading) {
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

                        // 모두 미루기 버튼
                        item {
                            Button(
                                onClick = {
                                    isShowDialog = !isShowDialog
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White,
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "모두 미루기",
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            }

                            // 모두 미루기 다이얼로그
                            if (isShowDialog) {
                                BasicDialog(
                                    dialogType = DialogMod.TODO_ALL_DELAY,
                                    showDialog = isShowDialog,
                                    onDismiss = {
                                        isShowDialog = false
                                    },
                                    onCancel = {
                                        isShowDialog = false
                                    },
                                    onConfirmed = {
                                        overdueViewModel.delayAllTodoItems()
                                    },
                                    title = "",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
