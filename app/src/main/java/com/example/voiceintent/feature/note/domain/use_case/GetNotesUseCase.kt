package com.example.voiceintent.feature.note.domain.use_case

import androidx.paging.PagingData
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
        tag: String? = null
    ): Flow<PagingData<Note>> =
        repository.getNotes(query, mood, tag)
}