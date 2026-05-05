package com.example.voiceintent.feature.note.domain.entity

import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage

data class Note(
    val id: Long = 0,
    val audioPath: String,
    val transcript: String,
    val summary: String,
    val mood: Mood,
    val tags: List<String>,
    val tasks: List<String>,
    val createdAt: Long,
    val durationMs: Long,
    val language: AudioLanguage
)
