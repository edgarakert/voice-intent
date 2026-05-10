package com.example.voiceintent.feature.note_details.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.voiceintent.R
import com.example.voiceintent.feature.note_details.presentation.viewmodel.PlayerState

@Composable
fun AudioPlayerRow(
    playerState: PlayerState,
    durationMs: Long,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalMs = when (playerState) {
        is PlayerState.Progress -> playerState.totalMs
        is PlayerState.Paused -> playerState.totalMs
        else -> durationMs
    }
    val currentMs = when (playerState) {
        is PlayerState.Progress -> playerState.currentMs
        is PlayerState.Paused -> playerState.currentMs
        else -> 0L
    }
    val isPlaying = playerState is PlayerState.Playing || playerState is PlayerState.Progress

    var isDragging by remember { mutableStateOf(false) }
    var dragValue by remember { mutableFloatStateOf(0f) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        AudioPlayerControllerButton(
            isPlaying = isPlaying,
            onPausePressed = onPause,
            onPlayPressed = onPlay
        )
        Slider(
            value = if (isDragging) dragValue else {
                if (totalMs > 0) currentMs.toFloat() / totalMs else 0f
            },
            onValueChange = {
                isDragging = true
                dragValue = it
            },
            onValueChangeFinished = {
                isDragging = false
                onSeek((dragValue * totalMs).toLong())
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${formatDuration(currentMs)} / ${formatDuration(totalMs)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AudioPlayerControllerButton(
    isPlaying: Boolean,
    onPausePressed: () -> Unit,
    onPlayPressed: () -> Unit
) {
    IconButton(onClick = if (isPlaying) onPausePressed else onPlayPressed) {
        Icon(
            painter = painterResource(
                if (isPlaying) R.drawable.ic_baseline_pause_24
                else R.drawable.ic_baseline_play_arrow_24
            ),
            contentDescription = stringResource(
                if (isPlaying) R.string.note_details_pause else R.string.note_details_play
            )
        )
    }
}

private fun formatDuration(ms: Long): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}