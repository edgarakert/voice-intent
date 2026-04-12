package com.example.voiceintent.feature.record.presentation.screen

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import java.util.jar.Manifest

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
        if (isGranted) {
            recordControl?.start(AudioLanguage.Auto)
            viewModel.onRecordingStarted()
        } else {
            viewModel.onRecordingError(message = "Нет доступа к микрофону")
        }
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
            TopAppBar(
                title = { Text(text = "Новая заметка") },
                navigationIcon = {
                    IconButton(onClick = onRecordingDone) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
                actions = {
                    if (state is RecordState.Recording) {
                        RecordIndicator()
                    }
                }
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            when (val s = state) {
                is RecordState.Idle -> {
                    Text(
                        text = "Нажми, чтобы начать запись",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(24.dp))
                    RecordButton(isRecording = false) {
                        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                    }
                }

                is RecordState.Recording -> {
                    Timer(durationMs = s.durationMs)
                    Spacer(Modifier.height(24.dp))
                    AudioWaveform(
                        amplitudeLevel = s.amplitudeLevel, modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                    RecordButton(isRecording = true) {
                        val record = recordControl?.stop()
                        if (record != null) {
                            viewModel.onRecordingStopped(record = record)
                        } else {
                            viewModel.onRecordingError(message = "Не удалось остановить запись")
                        }
                    }
                }

                is RecordState.Stopped -> {
                    Text(
                        text = "Готово — ${s.record.durationMs / 1000} сек",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is RecordState.Error -> {
                    Text(
                        text = "Ошибка: ${s.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}