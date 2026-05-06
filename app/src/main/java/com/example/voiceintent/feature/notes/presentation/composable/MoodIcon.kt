package com.example.voiceintent.feature.notes.presentation.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood

@Composable
fun MoodIcon(mood: Mood, modifier: Modifier = Modifier) {
    val emoji = when (mood) {
        Mood.POSITIVE -> "\uD83D\uDE0A"
        Mood.NEGATIVE -> "\uD83D\uDE14"
        Mood.NEUTRAL -> "\uD83D\uDE10"
    }

    Text(text = emoji, modifier = modifier.size(20.dp))
}