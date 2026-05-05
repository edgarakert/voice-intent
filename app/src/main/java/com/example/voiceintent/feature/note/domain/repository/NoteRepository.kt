package com.example.voiceintent.feature.note.domain.repository

import com.example.voiceintent.feature.note.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun saveNote(note: Note): Long

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?

    suspend fun deleteNote(note: Note)
}