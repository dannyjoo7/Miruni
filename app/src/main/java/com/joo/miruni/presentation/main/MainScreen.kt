package com.joo.miruni.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // import 추가
import androidx.navigation.compose.rememberNavController
import com.joo.miruni.R
import com.joo.miruni.presentation.BottomNavItem
import com.joo.miruni.presentation.Screen
import com.joo.miruni.presentation.calendar.CalendarScreen
import com.joo.miruni.presentation.home.HomeScreen
import com.joo.miruni.presentation.putoff.PutOffScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { "" },
                    navigationIcon = {
                        IconButton(onClick = {
                            // 메뉴 열기
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_menu),
                                contentDescription = "menu"
                            )
                        }
                    },
                )
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                BottomNavigationBar(
                    navController,
                    mainViewModel.bottomNavItems
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            NavigationHost(navController) // 네비게이션 호스트
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(item.iconResId),
                        contentDescription = item.label
                    )
                }, // 아이콘 리소스 ID를 사용
                label = { },
                selected = false,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.PutOff.route) { PutOffScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
    }
}

// 프리뷰 컴포저블
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    val previewViewModel = MainViewModel()

    MainScreen(navController, previewViewModel)
}


