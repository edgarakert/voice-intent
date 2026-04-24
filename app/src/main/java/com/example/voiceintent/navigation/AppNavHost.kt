package com.example.voiceintent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.voiceintent.feature.note_analysis.presentation.screen.NoteAnalysisScreen
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
        startDestination = Screen.Record.route,
        modifier = modifier
    ) {
        composable(route = Screen.Record.route) {
            RecordScreen(
                recordControl = recordControl,
                navigateBack = { navController.popBackStack() },
                onRecordingDone = { audioRecord ->
                    navController.navigate(
                        Screen.NoteAnalysis.createRoute(
                            audioPath = audioRecord.path,
                            language = audioRecord.language.code
                        )
                    )
                },
            )
        }

        composable(
            route = Screen.NoteAnalysis.route,
            arguments = listOf(
                navArgument("audio_path") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType }
            )
        ) {
            NoteAnalysisScreen(
                navigateBack = { navController.popBackStack() },
                onAnalysisDone = {
                    navController.navigate(Screen.Notes.route) {
                        popUpTo(Screen.Record.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}