package com.example.voiceintent.feature.note.domain.use_case

import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.repository.NoteRepository
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(
        query: String = "",
        mood: Mood? = null,
        offset: Int = 0
    ): Flow<List<Note>> =
        repository.getNotes(query, mood, offset)
}