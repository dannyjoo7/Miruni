package com.joo.miruni.presentation.addTask.addTodo


import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class AddTodoActivity : ComponentActivity() {
    private val addTodoViewModel: AddTodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 꺼내기
        val selectedDate: LocalDate? = intent.getStringExtra("SELECT_DATE")?.let { LocalDate.parse(it) }

        initModel(selectedDate)

        // 상태바 설정
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.WHITE, Color.WHITE
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.WHITE, Color.WHITE
            )
        )

        setContent {
            val navController = rememberNavController()
            AddTodoScreen(addTodoViewModel, selectedDate)
        }
    }

    private fun initModel(selectedDate: LocalDate?){
        if (selectedDate != null){
            addTodoViewModel.setSelectedDate(selectedDate)
        }
    }
}