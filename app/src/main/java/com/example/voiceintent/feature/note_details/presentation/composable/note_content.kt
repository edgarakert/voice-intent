package com.example.voiceintent.feature.note_details.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.voiceintent.feature.note_details.presentation.viewmodel.NoteDetailsState
import com.example.voiceintent.feature.notes.presentation.composable.MoodIcon
import com.example.voiceintent.feature.notes.presentation.composable.NoteTag
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteContent(
    state: NoteDetailsState.Success,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val note = state.note
    val tags = note.tags
    val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = dateFormat.format(Date(note.createdAt)),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(6.dp))
            MoodIcon(mood = note.mood)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = note.summary,
            style = MaterialTheme.typography.bodyLarge
        )
        if (tags.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(tags) { tag ->
                    NoteTag(tag = tag, onClick = {})
                }
            }
        }
        if (note.tasks.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            TasksSection(tasks = note.tasks)
        }
        Spacer(Modifier.height(16.dp))
        TranscriptSection(transcript = note.transcript)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        AudioPlayerRow(
            playerState = state.playerState,
            durationMs = note.durationMs,
            onPlay = onPlay,
            onPause = onPause,
            onSeek = onSeek
        )
        Spacer(Modifier.height(16.dp))
    }
}