package com.example.voiceintent.feature.note_analysis.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)
