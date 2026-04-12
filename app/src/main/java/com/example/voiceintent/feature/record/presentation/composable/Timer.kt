package com.example.voiceintent.feature.record.presentation.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Timer(durationMs: Long) {
    val seconds = (durationMs / 1000) % 60
    val minutes = durationMs / 1000 / 60
    val formattedText = "%02d:%02d".format(minutes, seconds)

    Text(
        text = formattedText,
        style = MaterialTheme.typography.displayMedium,
    )
}