package com.example.voiceintent.feature.note_analysis.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voiceintent.R
import com.example.voiceintent.feature.note_analysis.presentation.viewmodel.NoteAnalysisState
import com.example.voiceintent.feature.note_analysis.presentation.viewmodel.NoteAnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteAnalysisScreen(
    navigateBack: () -> Unit,
    onAnalysisDone: () -> Unit,
    viewModel: NoteAnalysisViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state is NoteAnalysisState.Done) {
            onAnalysisDone()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Обработка заметки") },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        enabled = state is NoteAnalysisState.Error
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { _ ->
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            },
            contentKey = { state ->
                when (state) {
                    is NoteAnalysisState.Processing -> "processing"
                    is NoteAnalysisState.Done -> "done"
                    is NoteAnalysisState.Error -> "error"
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp),
            label = "note_analysis_state"
        ) { state ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                when (state) {
                    is NoteAnalysisState.Processing -> ProcessingState()
                    is NoteAnalysisState.Error -> ErrorState(
                        onNavigateBack = navigateBack,
                        onRetry = { viewModel.retryProcessNote() },
                        message = state.message
                    )

                    is NoteAnalysisState.Done -> {}
                }
            }
        }
    }
}

@Composable
private fun ProcessingState() {
    CircularProgressIndicator(
        modifier = Modifier.size(56.dp),
        strokeWidth = 3.dp
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Обрабатываю заметку...",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Icon(
        painter = painterResource(R.drawable.ic_rounded_error_24),
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        tint = MaterialTheme.colorScheme.error
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = "Не удалось обработать заметку")
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onNavigateBack
        ) {
            Text("Отмена")
        }
        Button(
            onClick = onRetry
        ) {
            Text("Повторить")
        }
    }
}