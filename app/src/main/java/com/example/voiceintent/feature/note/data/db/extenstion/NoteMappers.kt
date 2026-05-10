package com.example.voiceintent.feature.note.data.db.extenstion

import com.example.voiceintent.feature.note.data.db.NoteEntity
import com.example.voiceintent.feature.note.data.db.NoteWithTags
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note_analysis.domain.entity.Mood
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import kotlinx.serialization.json.Json

fun Note.toEntity() = NoteEntity(
    id = id,
    audioPath = audioPath,
    transcript = transcript,
    summary = summary,
    mood = mood.name,
    tasks = Json.encodeToString(tasks),
    createdAt = createdAt,
    durationMs = durationMs,
    language = language.code
)

fun NoteEntity.toDomain() = Note(
    id = id,
    audioPath = audioPath,
    transcript = transcript,
    summary = summary,
    mood = Mood.valueOf(mood),
    tags = emptyList(),
    tasks = Json.decodeFromString(tasks),
    createdAt = createdAt,
    durationMs = durationMs,
    language = AudioLanguage.fromCode(language)
)

fun NoteWithTags.toDomain() = note.toDomain().copy(
    tags = tags.map { it.name }
)
