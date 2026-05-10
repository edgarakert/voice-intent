package com.example.voiceintent.feature.note.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(note: NoteEntity): Long

    @Transaction
    @Query(
        """
        SELECT DISTINCT n.* FROM notes n
        WHERE (:query = ''
           OR LOWER(n.transcript) LIKE '%' || LOWER(:query) || '%'
           OR LOWER(n.summary) LIKE '%' || LOWER(:query) || '%'
           OR n.id IN (
               SELECT nt.noteId FROM noteTags nt
               JOIN tags t ON nt.tagId = t.id
               WHERE LOWER(t.name) LIKE '%' || LOWER(:query) || '%'
           ))
        AND (:mood = '' OR n.mood = :mood)
        ORDER BY n.createdAt DESC
        LIMIT 20 OFFSET :offset
    """
    )
    fun getNotes(
        query: String = "",
        mood: String = "",
        offset: Int = 0
    ): Flow<List<NoteWithTags>>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Long): NoteWithTags?

    @Delete
    suspend fun delete(note: NoteEntity)
}