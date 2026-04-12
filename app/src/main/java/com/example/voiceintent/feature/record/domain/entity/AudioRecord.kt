package com.example.voiceintent.feature.record.domain.entity

data class AudioRecord(
    val path: String,
    val durationMs: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val language: AudioLanguage = AudioLanguage.Auto,
)
