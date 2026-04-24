package com.example.voiceintent.feature.note_analysis.data.api.extension

import com.example.voiceintent.feature.note_analysis.domain.entity.Mood

fun String.toMood(): Mood = when (lowercase()) {
    "positive" -> Mood.POSITIVE
    "negative" -> Mood.NEGATIVE
    else -> Mood.NEUTRAL
}