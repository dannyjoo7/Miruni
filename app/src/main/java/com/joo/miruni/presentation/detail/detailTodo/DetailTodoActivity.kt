package com.joo.miruni.presentation.detail.detailTodo


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTodoActivity : ComponentActivity() {
    private val detailTodoViewModel: DetailTodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoId = intent.getLongExtra("TODO_ID", -1)

        if (todoId == -1L) {
            Toast.makeText(this.applicationContext, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            initModel(todoId)
        }

        // 상태바 설정
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            DetailTodoScreen(detailTodoViewModel)
        }
    }

    private fun initModel(todoId: Long) {
        detailTodoViewModel.loadTodoDetails(todoId)
    }
}