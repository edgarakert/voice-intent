package com.example.voiceintent.feature.note.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query(
        """
        SELECT * FROM notes
        WHERE LOWER(transcript) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(tags) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(summary) LIKE '%' || LOWER(:query) || '%'
        ORDER BY createdAt DESC
    """
    )
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE mood = :mood ORDER BY createdAt DESC")
    fun getNotesByMood(mood: String): Flow<List<NoteEntity>>

    @Delete
    suspend fun delete(note: NoteEntity)
}