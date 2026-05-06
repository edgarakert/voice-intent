package com.example.voiceintent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voiceintent.feature.note_analysis.presentation.screen.NoteAnalysisScreen
import com.example.voiceintent.feature.notes.presentation.screen.NotesScreen
import com.example.voiceintent.feature.record.presentation.screen.RecordScreen
import com.example.voiceintent.feature.record.presentation.service.RecordControl

@Composable
fun AppNavHost(
    navController: NavHostController,
    recordControl: RecordControl?,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Record.route
        ) {
            RecordScreen(
                recordControl = recordControl,
                navigateBack = { navController.popBackStack() },
                onRecordingDone = {
                    navController.navigate(
                        Screen.NoteAnalysis.route
                    )
                },
            )
        }

        composable(
            route = Screen.NoteAnalysis.route,
        ) {
            NoteAnalysisScreen(
                navigateBack = { navController.popBackStack() },
                onAnalysisDone = {
                    navController.navigate(Screen.Notes.route) {
                        popUpTo(Screen.Record.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Notes.route
        ) {
            NotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteDetails.createRoute(noteId))
                },
                onCreateNote = {
                    navController.navigate(Screen.Record.route)
                }
            )
        }
    }
}