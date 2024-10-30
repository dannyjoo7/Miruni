package com.joo.miruni.presentation

enum class Screen(val route: String) {
    Home("home"),
    Overdue("overdue"),
    Calendar("calendar"),
}

data class BottomNavItem(
    val label: String,
    val iconResId: Int,
    val screen: Screen,
)

