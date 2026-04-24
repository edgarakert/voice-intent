package com.example.voiceintent.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Notes : Screen(route = "notes")
    data object Record : Screen(route = "record")
    data object Details : Screen(route = "detail/{noteId}") {
        fun createRoute(noteId: Long) = "detail/$noteId"
    }

    data object NoteAnalysis : Screen(route = "note_analysis/{audio_path}/{language}") {
        fun createRoute(audioPath: String, language: String): String =
            "note_analysis/${Uri.encode(audioPath)}/$language"
    }

    data object Settings : Screen(route = "settings")
}