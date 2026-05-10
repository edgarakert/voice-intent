package com.example.voiceintent.feature.note_details.presentation.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.voiceintent.R

@Composable
fun TranscriptSection(transcript: String) {
    var expanded by remember { mutableStateOf(false) }
    var needsExpansion by remember { mutableStateOf(false) }

    Text(
        text = stringResource(R.string.note_details_transcript),
        style = MaterialTheme.typography.titleSmall
    )
    Spacer(Modifier.height(6.dp))
    Text(
        text = transcript,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = if (expanded) Int.MAX_VALUE else 4,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { result ->
            if (!expanded) needsExpansion = result.hasVisualOverflow
        }
    )
    if (needsExpansion || expanded) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(
                text = stringResource(
                    if (expanded) R.string.note_details_hide_transcript
                    else R.string.note_details_show_full_transcript
                )
            )
        }
    }
}
