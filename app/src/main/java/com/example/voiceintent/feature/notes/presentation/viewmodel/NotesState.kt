package com.example.voiceintent.feature.notes.presentation.viewmodel

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
    val label: String,
    val value: Mood?
) {
    ALL("Все", null),
    POSITIVE("\uD83D\uDE0A Позитив", Mood.POSITIVE),
    NEUTRAL("\uD83D\uDE10 Нейтрально", Mood.NEUTRAL),
    NEGATIVE("\uD83D\uDE14 Негатив", Mood.NEGATIVE),
}
