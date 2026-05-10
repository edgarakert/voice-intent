package com.example.voiceintent.feature.note_details.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.voiceintent.R

@Composable
fun TasksSection(tasks: List<String>) {
    Text(
        text = stringResource(R.string.note_details_tasks),
        style = MaterialTheme.typography.titleSmall
    )
    Spacer(Modifier.height(6.dp))
    tasks.forEach { task ->
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(text = "• ", style = MaterialTheme.typography.bodyMedium)
            Text(text = task, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
