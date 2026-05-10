package com.example.voiceintent.feature.notes.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.voiceintent.feature.notes.presentation.viewmodel.MoodFilter

@Composable
fun MoodFilterChips(
    selectedFilter: MoodFilter,
    onFilterSelect: (MoodFilter) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(MoodFilter.entries.toList()) { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelect(filter) },
                label = { Text(text = stringResource(filter.labelRes)) }
            )
        }
    }
}