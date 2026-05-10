package com.example.voiceintent.feature.note.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val audioPath: String,
    val transcript: String,
    val summary: String,
    val mood: String,
    val tasks: String,
    val createdAt: Long,
    val durationMs: Long,
    val language: String
)