package com.example.voiceintent.feature.note.domain.repository

import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun saveNote(note: Note): Long

    fun getNotes(
        query: String = "",
        mood: Mood? = null,
        offset: Int = 0
    ): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?

    suspend fun deleteNote(note: Note)
}