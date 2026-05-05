package com.example.voiceintent.navigation

sealed class Screen(val route: String) {
    data object Notes : Screen(route = "notes")

    data object Record : Screen(route = "record")

    data object Details : Screen(route = "detail/{noteId}") {
        fun createRoute(noteId: Long) = "detail/$noteId"
    }

    data object NoteAnalysis : Screen(route = "note_analysis")

    data object Settings : Screen(route = "settings")
}