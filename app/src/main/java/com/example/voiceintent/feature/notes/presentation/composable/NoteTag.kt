package com.example.voiceintent.feature.notes.presentation.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NoteTag(tag: String, onClick: () -> Unit) {
    SuggestionChip(
        onClick = onClick,
        label = {
            Text(
                text = "#$tag",
                style = MaterialTheme.typography.labelSmall
            )
        }
    )
}