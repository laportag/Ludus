package com.doorxii.ludus.utils.ui

sealed class LudusManagementRoutes (val route: String) {
    object Home: LudusManagementRoutes("home")
    object Barracks: LudusManagementRoutes("barracks")
    object Market: LudusManagementRoutes("market")
    object CombatSelect: LudusManagementRoutes("combat_select")
    object Settings: LudusManagementRoutes("settings")
}