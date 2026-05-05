package com.example.voiceintent.feature.note.data.repository

import com.example.voiceintent.di.IODispatcher
import com.example.voiceintent.feature.note.data.db.NoteDao
import com.example.voiceintent.feature.note.data.db.extenstion.toDomain
import com.example.voiceintent.feature.note.data.db.extenstion.toEntity
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : NoteRepository {
    override suspend fun saveNote(note: Note): Long = withContext(ioDispatcher) {
        return@withContext dao.insert(note.toEntity())
    }

    override fun getAllNotes(): Flow<List<Note>> = dao.getAllNotes().map { list ->
        list.map { it.toDomain() }
    }.flowOn(ioDispatcher)


    override suspend fun getNoteById(id: Long): Note? = withContext(ioDispatcher) {
        return@withContext dao.getNoteById(id)?.toDomain()
    }

    override suspend fun deleteNote(note: Note) = withContext(ioDispatcher) {
        return@withContext dao.delete(note.toEntity())
    }
}