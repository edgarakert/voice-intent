package com.example.voiceintent.feature.note.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.example.voiceintent.di.IODispatcher
import com.example.voiceintent.feature.note.data.db.NoteDao
import com.example.voiceintent.feature.note.data.db.NoteDatabase
import com.example.voiceintent.feature.tag.data.db.NoteTagEntity
import com.example.voiceintent.feature.tag.data.db.TagEntity
import com.example.voiceintent.feature.note.data.db.extenstion.toDomain
import com.example.voiceintent.feature.note.data.db.extenstion.toEntity
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.repository.NoteRepository
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import com.example.voiceintent.feature.tag.data.db.TagDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val tagDao: TagDao,
    private val database: NoteDatabase,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : NoteRepository {
    override suspend fun saveNote(note: Note): Long = withContext(ioDispatcher) {
        database.withTransaction {
            val noteId = noteDao.insert(note.toEntity())
            note.tags.forEach { tagName ->
                tagDao.insertTag(TagEntity(name = tagName))
                val tagId = tagDao.getTagIdByName(tagName)!!
                tagDao.insertNoteTag(NoteTagEntity(noteId = noteId, tagId = tagId))
            }
            noteId
        }
    }

    override fun getNotes(
        query: String,
        mood: Mood?,
        tag: String?
    ): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { noteDao.getNotes(query, mood?.name ?: "", tag ?: "") }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() } }

    override suspend fun getNoteById(id: Long): Note? =
        withContext(ioDispatcher) { noteDao.getNoteById(id)?.toDomain() }

    override suspend fun deleteNote(note: Note) = withContext(ioDispatcher) {
        noteDao.delete(note.toEntity())
    }
}