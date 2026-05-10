package com.example.voiceintent.feature.notes.presentation.viewmodel

import androidx.annotation.StringRes
import com.example.voiceintent.R
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood

data class NotesState(
    val notes: List<Note> = emptyList(),
    val filteredNotes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val selectedMoodFilter: MoodFilter = MoodFilter.ALL,
    val selectedTagsFilter: String? = null,
    val isLoading: Boolean = true,
    val allTags: List<String> = emptyList()
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
