package com.example.voiceintent.feature.note_details.domain.use_case

import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteDetailsUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note =
        repository.getNoteById(id) ?: throw IllegalStateException("note == null")
}