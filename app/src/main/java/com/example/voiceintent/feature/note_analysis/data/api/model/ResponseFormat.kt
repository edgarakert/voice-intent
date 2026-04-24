package com.example.voiceintent.feature.note_analysis.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseFormat(
    val type: String = "json_object"
)
