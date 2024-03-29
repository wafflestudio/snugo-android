package com.wafflestudio.snugo.navigation

import androidx.navigation.NamedNavArgument

sealed class NavigationDestination(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Onboarding : NavigationDestination("onboarding")

    object Main : NavigationDestination("main")

    object SignIn : NavigationDestination("signin")

    object Home : NavigationDestination("home")

    object ArrivalDetail : NavigationDestination("arrival_detail")

    object Records : NavigationDestination("record")

    object RecordMap : NavigationDestination("recordmap")

    object Settings : NavigationDestination("settings")
}
