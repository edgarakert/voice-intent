package com.example.voiceintent.feature.note_details.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voiceintent.R
import com.example.voiceintent.feature.note_details.presentation.composable.NoteContent
import com.example.voiceintent.feature.note_details.presentation.viewmodel.NoteDetailsState
import com.example.voiceintent.feature.note_details.presentation.viewmodel.NoteDetailsViewModel

@Composable
fun NoteDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is NoteDetailsState.Deleted) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onNavigateBack = onNavigateBack,
                showDeleteButton = state is NoteDetailsState.Success
            ) { showDeleteDialog = true }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val s = state) {
                is NoteDetailsState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                is NoteDetailsState.Error -> Text(
                    text = s.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )

                is NoteDetailsState.Success -> NoteContent(
                    state = s,
                    onPlay = viewModel::playAudio,
                    onPause = viewModel::pauseAudio,
                    onSeek = viewModel::seekTo
                )

                is NoteDetailsState.Deleted -> Unit
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissButtonPressed = { showDeleteDialog = false },
            onConfirmButtonPressed = {
                showDeleteDialog = false
                viewModel.deleteNote()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onNavigateBack: () -> Unit,
    showDeleteButton: Boolean,
    onDeleteButtonClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.note_details_note))
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.note_details_back)
                )
            }
        },
        actions = {
            if (showDeleteButton) {
                IconButton(onClick = onDeleteButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.note_details_delete)
                    )
                }
            }
        },
    )
}

@Composable
private fun AlertDialog(
    onDismissButtonPressed: () -> Unit,
    onConfirmButtonPressed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissButtonPressed,
        title = { Text(stringResource(R.string.note_details_delete_confirmation_title)) },
        text = { Text(stringResource(R.string.note_details_delete_confirmation_body)) },
        confirmButton = {
            TextButton(onClick = onConfirmButtonPressed) {
                Text(stringResource(R.string.note_details_confirm_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonPressed) {
                Text(stringResource(R.string.note_details_cancel))
            }
        }
    )
}