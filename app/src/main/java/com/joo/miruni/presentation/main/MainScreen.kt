package com.joo.miruni.presentation.main

import androidx.compose.foundation.layout.*
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
import androidx.navigation.compose.composable
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
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // 메뉴 열기
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = "menu",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController,
                mainViewModel.bottomNavItems
            )
        },
        containerColor = Color.White
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            NavigationHost(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    val currentDestination = navController.currentDestination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(item.iconResId),
                        contentDescription = item.label
                    )
                },
                label = { },
                selected = currentDestination == item.screen.route,
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
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.PutOff.route) { PutOffScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        val previewViewModel = MainViewModel()

        MainScreen(navController, previewViewModel)
    }
}


