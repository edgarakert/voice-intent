package com.example.voiceintent.navigation

sealed class Screen(val route: String) {
    object Notes : Screen(route = "notes")
    object Record : Screen(route = "record")
    object Details : Screen(route = "detail/{noteId}") {
        fun createRoute(noteId: Long) = "detail/$noteId"
    }

    object Analytics : Screen(route = "analytics")
    object Settings : Screen(route = "settings")
}