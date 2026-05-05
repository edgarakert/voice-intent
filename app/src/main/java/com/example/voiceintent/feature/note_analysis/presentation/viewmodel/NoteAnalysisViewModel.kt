package com.example.voiceintent.feature.note_analysis.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.use_case.SaveNoteUseCase
import com.example.voiceintent.feature.note_analysis.domain.use_case.ProcessVoiceNoteUseCase
import com.example.voiceintent.feature.record.domain.use_case.GetAudioRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NoteAnalysisViewModel @Inject constructor(
    private val processVoiceNoteUseCase: ProcessVoiceNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getAudioRecordUseCase: GetAudioRecordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<NoteAnalysisState>(value = NoteAnalysisState.Processing)
    val state: StateFlow<NoteAnalysisState> = _state.asStateFlow()

    init {
        processNote()
    }

    private fun processNote() {
        viewModelScope.launch {
            _state.value = NoteAnalysisState.Processing

            try {
                val (path, durationMs, createdAt, language) = getAudioRecordUseCase.invoke()
                    ?: throw IllegalStateException("AudioRecord не найден")


                val audioFile = File(path)

                val result = processVoiceNoteUseCase.invoke(
                    audioFile = audioFile,
                    language = language
                )

                saveNoteUseCase.invoke(
                    Note(
                        audioPath = path,
                        transcript = result.transcript,
                        summary = result.summary,
                        mood = result.mood,
                        tags = result.tags,
                        tasks = result.tags,
                        createdAt = createdAt,
                        durationMs = durationMs,
                        language = language
                    )
                )

                _state.value = NoteAnalysisState.Done(result = result)
            } catch (e: Exception) {
                _state.value = NoteAnalysisState.Error(
                    message = e.message ?: "Не удалось обработать заметку"
                )
            }
        }
    }

    fun retryProcessNote() {
        processNote()
    }
}