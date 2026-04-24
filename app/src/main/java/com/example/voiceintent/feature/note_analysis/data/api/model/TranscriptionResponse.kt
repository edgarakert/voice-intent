package com.example.voiceintent.feature.note_analysis.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TranscriptionResponse(
    val text: String
)
