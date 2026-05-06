package com.example.voiceintent.navigation

sealed class Screen(val route: String) {
    data object Notes : Screen(route = "notes")

    data object Record : Screen(route = "record")

    data object NoteDetails : Screen(route = "note_details/{noteId}") {
        fun createRoute(noteId: Long) = "note_details/$noteId"
    }

    data object NoteAnalysis : Screen(route = "note_analysis")

    data object Settings : Screen(route = "settings")
}