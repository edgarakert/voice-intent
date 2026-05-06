package com.example.voiceintent.feature.note.domain.repository

import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun saveNote(note: Note): Long

    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?

    fun searchNotes(query: String): Flow<List<Note>>

    fun getNotesByMood(mood: Mood): Flow<List<Note>>

    suspend fun deleteNote(note: Note)
}