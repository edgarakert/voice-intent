package com.example.voiceintent.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        composable(Screen.Record.route) {
            RecordScreen(
                recordControl = recordControl,
                onRecordingDone = {
                    navController.popBackStack()
                }
            )
        }
    }
}