package com.example.voiceintent.feature.note_analysis.domain.entity

data class NoteAnalysisResult(
    val transcript: String,
    val tags: List<String>,
    val tasks: List<String>,
    val mood: Mood,
    val summary: String,
)
