package com.example.voiceintent.feature.notes.presentation.viewmodel

import androidx.annotation.StringRes
import com.example.voiceintent.R
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood

data class NotesState(
    val searchQuery: String = "",
    val selectedMoodFilter: MoodFilter = MoodFilter.ALL,
    val selectedTagsFilter: String? = null,
)

enum class MoodFilter(
    @param:StringRes val labelRes: Int,
    val value: Mood?
) {
    ALL(R.string.notes_mood_all, null),
    POSITIVE(R.string.notes_mood_positive, Mood.POSITIVE),
    NEUTRAL(R.string.notes_mood_neutral, Mood.NEUTRAL),
    NEGATIVE(R.string.notes_mood_negative, Mood.NEGATIVE),
}
