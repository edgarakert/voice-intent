package com.example.voiceintent.feature.note_analysis.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteAnalysis(
    val tags: List<String>,
    val tasks: List<String>,
    val mood: String,
    val summary: String,
)
