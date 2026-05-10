package com.example.voiceintent.feature.note.domain.repository

import androidx.paging.PagingData
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun saveNote(note: Note): Long

    fun getNotes(
        query: String = "",
        mood: Mood? = null,
        tag: String? = null
    ): Flow<PagingData<Note>>

    suspend fun getNoteById(id: Long): Note?

    suspend fun deleteNote(note: Note)
}