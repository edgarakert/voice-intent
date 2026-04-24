package com.example.voiceintent.feature.note_analysis.domain.use_case

import com.example.voiceintent.di.IODispatcher
import com.example.voiceintent.feature.note_analysis.domain.entity.NoteAnalysisResult
import com.example.voiceintent.feature.note_analysis.domain.repository.NoteAnalysisRepository
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ProcessVoiceNoteUseCase @Inject constructor(
    private val repository: NoteAnalysisRepository,
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        audioFile: File,
        language: AudioLanguage
    ): NoteAnalysisResult =
        withContext(dispatcher) {
            val transcript = repository.transcribe(
                audioFile = audioFile,
                language = language
            )

            return@withContext repository.analyze(transcript)
        }
}