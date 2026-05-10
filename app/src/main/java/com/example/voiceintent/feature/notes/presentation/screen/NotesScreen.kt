package com.example.voiceintent.feature.notes.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voiceintent.R
import com.example.voiceintent.feature.notes.presentation.composable.MoodFilterChips
import com.example.voiceintent.feature.notes.presentation.composable.NoteCard
import com.example.voiceintent.feature.notes.presentation.composable.NotesSearchBar
import com.example.voiceintent.feature.notes.presentation.viewmodel.MoodFilter
import com.example.voiceintent.feature.notes.presentation.viewmodel.NotesViewModel

@Composable
fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onCreateNote: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNote) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.notes_create_note)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.notes_notes),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            NotesSearchBar(
                query = state.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged
            )
            Spacer(modifier = Modifier.height(8.dp))
            MoodFilterChips(
                selectedFilter = state.selectedMoodFilter,
                onFilterSelect = viewModel::onMoodFilterChanged
            )
            if (state.selectedTagsFilter != null) {
                Spacer(modifier = Modifier.height(4.dp))
                ActiveTagFilter(
                    tag = state.selectedTagsFilter!!,
                    onClear = { viewModel.onTagFilterChanged(null) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.filteredNotes.isEmpty() -> {
                    EmptyNotesPlaceholder(
                        hasFilters = state.searchQuery.isNotBlank()
                                || state.selectedMoodFilter != MoodFilter.ALL
                                || state.selectedTagsFilter != null,
                        onClearFilters = viewModel::onClearFilters
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = state.filteredNotes,
                            key = { it.id }
                        ) { note ->
                            NoteCard(
                                note = note,
                                onTagClick = viewModel::onTagFilterChanged,
                                onClick = { onNoteClick(note.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveTagFilter(tag: String, onClear: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.notes_tag),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        AssistChip(
            onClick = onClear,
            label = { Text(text = "#$tag") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.notes_clear_tag),
                    modifier = Modifier.size(16.dp)
                )
            }
        )
    }
}

@Composable
fun EmptyNotesPlaceholder(hasFilters: Boolean, onClearFilters: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(
                    if (hasFilters) R.string.notes_not_found
                    else
                        R.string.notes_no_notes
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    if (hasFilters)
                        R.string.notes_try_to_change_query
                    else
                        R.string.notes_record_first_note
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (hasFilters) {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onClearFilters) {
                    Text(text = stringResource(R.string.notes_clear_filters))
                }
            }
        }
    }
}