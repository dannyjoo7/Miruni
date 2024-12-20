package com.joo.miruni.presentation.addTask.addTodo


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoActivity : ComponentActivity() {
    private val addTodoViewModel: AddTodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상태바 설정
        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContent {
            val navController = rememberNavController()
            AddTodoScreen(addTodoViewModel)
        }
    }
}