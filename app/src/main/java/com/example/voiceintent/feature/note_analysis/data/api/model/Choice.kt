package com.example.voiceintent.feature.note_analysis.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val message: ChatMessage
)
