package com.thoriq.flog.data

sealed class Screen(val route: String) {
    object Home : Screen("HOME")
    object Fishes : Screen("FISHES")
    object Camera : Screen("CAMERA")
    object Maps : Screen("MAPS")
    object Account : Screen("ACCOUNT")
}