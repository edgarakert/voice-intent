package com.example.voiceintent.feature.note_analysis.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.di.IODispatcher
import com.example.voiceintent.feature.note_analysis.domain.use_case.ProcessVoiceNoteUseCase
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NoteAnalysisViewModel @Inject constructor(
    private val processVoiceNoteUseCase: ProcessVoiceNoteUseCase,
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow<NoteAnalysisState>(value = NoteAnalysisState.Processing)
    val state: StateFlow<NoteAnalysisState> = _state.asStateFlow()

    private val audioPath: String = checkNotNull(savedStateHandle["audio_path"])
    private val language: String = checkNotNull(savedStateHandle["language"])

    init {
        processNote()
    }

    private fun processNote() {
        viewModelScope.launch(dispatcher) {
            _state.value = NoteAnalysisState.Processing

            try {
                val audioFile = File(audioPath)

                val result = processVoiceNoteUseCase.invoke(
                    audioFile = audioFile,
                    language = AudioLanguage.fromCode(language)
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