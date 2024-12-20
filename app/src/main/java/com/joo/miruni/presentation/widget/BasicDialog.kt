package com.joo.miruni.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.joo.miruni.R


enum class DialogMod {
    TODO_DELETE,                // 할 일 삭제
    TODO_COMPLETE,              // 할 일 완료
    TODO_CANCEL_COMPLETE,       // 할 일 완료 취소
    TODO_ALL_DELAY,             // 할 일 모두 미루기
    SCHEDULE_DELETE,            // 일정 삭제
    SCHEDULE_COMPLETE,          // 일정 완료
    SCHEDULE_CANCEL_COMPLETE,   // 일정 완료 취소
}

// 대화상자
@Composable
fun BasicDialog(
    dialogType: DialogMod,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirmed: () -> Unit,
    title: String,
) {
    val dialogTitle: String
    val dialogContent: String
    val cancelButtonText: String
    val confirmButtonText: String

    when (dialogType) {
        DialogMod.TODO_DELETE -> {
            dialogTitle = "할 일 삭제"
            dialogContent = "$title \n 항목을 정말로 삭제하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "삭제"
        }

        DialogMod.TODO_COMPLETE -> {
            dialogTitle = "할 일 완료"
            dialogContent = "$title \n 항목을 정말로 완료하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "완료"
        }

        DialogMod.TODO_CANCEL_COMPLETE -> {
            dialogTitle = "완료 취소"
            dialogContent = "$title \n 항목을 정말로 완료 취소하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "완료 취소"
        }

        DialogMod.TODO_ALL_DELAY -> {
            dialogTitle = "모두 미루기"
            dialogContent = "모든 항목을 정말로 미루겠습니까??"
            cancelButtonText = "취소"
            confirmButtonText = "확인"
        }

        DialogMod.SCHEDULE_DELETE -> {
            dialogTitle = "일정 삭제"
            dialogContent = "$title \n 항목을 정말로 삭제하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "삭제"
        }

        DialogMod.SCHEDULE_COMPLETE -> {
            dialogTitle = "일정 완료"
            dialogContent = "$title \n 항목을 정말로 완료하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "완료"
        }

        DialogMod.SCHEDULE_CANCEL_COMPLETE -> {
            dialogTitle = "완료 취소"
            dialogContent = "$title \n 항목을 정말로 완료 취소하시겠습니까?"
            cancelButtonText = "취소"
            confirmButtonText = "완료 취소"
        }

        else -> {
            dialogTitle = "title"
            dialogContent = " "
            cancelButtonText = " "
            confirmButtonText = " "
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 240.dp)
                    .width(260.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(colorResource(R.color.ios_dialog_gray)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 제목
                    Row(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            text = dialogTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    }

                    // 내용
                    Row(
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = dialogContent,
                            textAlign = TextAlign.Center,
                        )
                    }

                    // 버튼
                    HorizontalDivider(
                        thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // 왼쪽 버튼
                        Column(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onConfirmed()
                                    onDismiss()
                                }
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = confirmButtonText,
                                color = colorResource(R.color.ios_blue),
                                textAlign = TextAlign.Center
                            )
                        }

                        // 구분선
                        Spacer(modifier = Modifier.width(4.dp))
                        VerticalDivider(
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.height(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        // 오른쪽 버튼
                        Column(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onCancel()
                                    onDismiss()
                                }
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = cancelButtonText,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicDialogPreview() {
    // 미리보기용 다이얼로그
    BasicDialog(
        dialogType = DialogMod.TODO_COMPLETE,
        showDialog = true,
        onDismiss = { },
        onCancel = { },
        onConfirmed = { },
        title = "일정 제목"
    )
}
