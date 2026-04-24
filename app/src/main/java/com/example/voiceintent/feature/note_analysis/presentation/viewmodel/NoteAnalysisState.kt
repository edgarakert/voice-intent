package com.example.voiceintent.feature.note_analysis.presentation.viewmodel

import com.example.voiceintent.feature.note_analysis.domain.entity.NoteAnalysisResult

sealed class NoteAnalysisState {
    data object Processing : NoteAnalysisState()

    data class Done(val result: NoteAnalysisResult) : NoteAnalysisState()

    data class Error(val message: String) : NoteAnalysisState()
}