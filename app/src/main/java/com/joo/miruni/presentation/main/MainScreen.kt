package com.joo.miruni.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joo.miruni.R
import com.joo.miruni.presentation.BottomNavItem
import com.joo.miruni.presentation.Screen
import com.joo.miruni.presentation.calendar.CalendarScreen
import com.joo.miruni.presentation.home.HomeScreen
import com.joo.miruni.presentation.putoff.PutOffScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
) {

    val isCompletedViewChecked = mainViewModel.settingObserveCompleteVisibility.observeAsState(
        false
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            // 메뉴
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row {
                            Text(
                                text = "미루니",
                                modifier = Modifier.padding(vertical = 8.dp),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "완료된 항목 보기",
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .weight(1f),
                                fontSize = 16.sp,
                            )

                            // 스위치
                            Switch(
                                modifier = Modifier
                                    .padding(end = 8.dp),
                                checked = isCompletedViewChecked.value,
                                onCheckedChange = {
                                    mainViewModel.setCompletedItemsVisibility()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedIconColor = Color.White,
                                    checkedTrackColor = Color(0xFF35C759),
                                    checkedBorderColor = Color(0xFF35C759),
                                    checkedThumbColor = Color.White,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color.LightGray,
                                    uncheckedBorderColor = Color.White,
                                ),
                            )
                        }
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text(text = "") },
                        navigationIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        scope.launch { drawerState.open() }
                                    }
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
            content = { contentPadding ->
                Box(
                    modifier = Modifier.padding(contentPadding)
                ) {
                    NavigationHost(navController)
                }
            },
            containerColor = Color.White,
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    val currentDestination = navController.currentDestination?.route

    Column {
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray
        )

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
}


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.PutOff.route) { PutOffScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
    }
}


