package com.joo.miruni.presentation

enum class Screen(val route: String) {
    Home("home"),
    PutOff("put off"),
    Calendar("calendar"),
}

data class BottomNavItem(
    val label: String,
    val iconResId: Int, // Int 타입으로 변경
    val screen: Screen,
)

