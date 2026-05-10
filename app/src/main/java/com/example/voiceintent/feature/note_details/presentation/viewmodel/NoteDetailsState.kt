package com.example.voiceintent.feature.note_details.presentation.viewmodel

import com.example.voiceintent.feature.note.domain.entity.Note

sealed class NoteDetailsState {
    data object Loading : NoteDetailsState()

    data class Success(
        val note: Note,
        val playerState: PlayerState = PlayerState.Idle
    ) : NoteDetailsState()

    data class Error(val message: String) : NoteDetailsState()

    data object Deleted : NoteDetailsState()
}

sealed class PlayerState {
    data object Idle : PlayerState()

    data object Playing : PlayerState()

    data class Progress(
        val currentMs: Long,
        val totalMs: Long
    ) : PlayerState()

    data class Paused(
        val currentMs: Long,
        val totalMs: Long
    ) : PlayerState()
}