package com.example.voiceintent.feature.note_analysis.domain.use_case

import com.example.voiceintent.feature.note_analysis.domain.entity.NoteAnalysisResult
import com.example.voiceintent.feature.note_analysis.domain.repository.NoteAnalysisRepository
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import java.io.File
import javax.inject.Inject

class ProcessVoiceNoteUseCase @Inject constructor(
    private val repository: NoteAnalysisRepository,
) {
    suspend operator fun invoke(
        audioFile: File,
        language: AudioLanguage
    ): NoteAnalysisResult {
        val transcript = repository.transcribe(
            audioFile = audioFile,
            language = language
        )

        return repository.analyze(transcript)
    }
}