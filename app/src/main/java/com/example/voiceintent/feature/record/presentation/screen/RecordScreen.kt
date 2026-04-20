package com.example.voiceintent.feature.record.presentation.screen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import com.example.voiceintent.feature.record.presentation.composable.AudioWaveform
import com.example.voiceintent.feature.record.presentation.composable.RecordButton
import com.example.voiceintent.feature.record.presentation.composable.RecordIndicator
import com.example.voiceintent.feature.record.presentation.composable.Timer
import com.example.voiceintent.feature.record.presentation.service.RecordControl
import com.example.voiceintent.feature.record.presentation.viewmodel.RecordState
import com.example.voiceintent.feature.record.presentation.viewmodel.RecordViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecordScreen(
    recordControl: RecordControl?,
    onRecordingDone: () -> Unit,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            viewModel.onRecordingError(message = "Нет доступа к микрофону")
            return@rememberLauncherForActivityResult
        }

        recordControl?.start(AudioLanguage.Auto)
        viewModel.onRecordingStarted()
    }

    LaunchedEffect(recordControl) {
        recordControl?.let {
            viewModel.onRecordEventsFlow(it.eventsFlow)
        }
    }

    LaunchedEffect(state) {
        if (state is RecordState.Stopped) {
            onRecordingDone()
        }
    }

    if (state is RecordState.Recording) {
        LaunchedEffect(Unit) {
            var elapsed = 0L
            while (true) {
                delay(100)
                elapsed += 100
                viewModel.updateDuration(durationMs = elapsed)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        if (state is RecordState.Recording) {
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        Text(text = "Новая заметка")
                        if (state is RecordState.Recording) {
                            Spacer(modifier = Modifier.width(8.dp))
                            RecordIndicator()
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onRecordingDone) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
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
                    is RecordState.Idle -> "idle"
                    is RecordState.Recording -> "recording"
                    is RecordState.Stopped -> "stopped"
                    is RecordState.Error -> "error"
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp), label = "record_state"
        ) { state ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when (val s = state) {
                    is RecordState.Idle -> {
                        IdleState {
                            permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                        }
                    }

                    is RecordState.Recording -> {
                        RecordingState(
                            durationMs = s.durationMs,
                            amplitudeLevel = s.amplitudeLevel
                        ) {
                            val record = recordControl?.stop()
                            if (record != null) {
                                viewModel.onRecordingStopped(record = record)
                            } else {
                                viewModel.onRecordingError(message = "Не удалось остановить запись")
                            }
                        }
                    }

                    is RecordState.Stopped -> {
                        StoppedState(durationSec = s.record.durationMs / 1000)
                    }

                    is RecordState.Error -> {
                        ErrorState(message = s.message)
                    }
                }
            }
        }
    }
}

@Composable
private fun IdleState(onRecordButtonClick: () -> Unit) {
    Text(
        text = "Нажми, чтобы начать запись",
        style = MaterialTheme.typography.bodyLarge
    )
    Spacer(Modifier.height(24.dp))
    RecordButton(isRecording = false, onClick = onRecordButtonClick)
}

@Composable
private fun RecordingState(
    durationMs: Long,
    amplitudeLevel: Float,
    onRecordButtonClick: () -> Unit
) {
    Timer(durationMs = durationMs)
    Spacer(Modifier.height(24.dp))
    AudioWaveform(
        amplitudeLevel = amplitudeLevel, modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )
    Spacer(Modifier.height(24.dp))
    RecordButton(isRecording = true, onClick = onRecordButtonClick)
}

@Composable
private fun StoppedState(durationSec: Long) {
    Text(
        text = "Готово — $durationSec сек",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun ErrorState(message: String) {
    Text(
        text = "Ошибка: $message",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}