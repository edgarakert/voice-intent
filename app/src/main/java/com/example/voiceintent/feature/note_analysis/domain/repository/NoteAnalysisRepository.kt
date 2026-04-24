package com.example.voiceintent.feature.note_analysis.domain.repository

import com.example.voiceintent.feature.note_analysis.domain.entity.NoteAnalysisResult
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import java.io.File

interface NoteAnalysisRepository {
    suspend fun transcribe(audioFile: File, language: AudioLanguage): String

    suspend fun analyze(transcript: String): NoteAnalysisResult
}