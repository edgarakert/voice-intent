package com.example.voiceintent.feature.record.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.di.DefaultDispatcher
import com.example.voiceintent.di.MainDispatcher
import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import com.example.voiceintent.feature.record.presentation.service.RecordEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    @param:MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow<RecordState>(RecordState.Idle)
    val state: StateFlow<RecordState> = _state.asStateFlow()

    fun onRecordEventsFlow(eventsFlow: Flow<RecordEvent>) {
        viewModelScope.launch(mainDispatcher) {
            withContext(defaultDispatcher) {
                eventsFlow.collect { event ->
                    when (event) {
                        is RecordEvent.AmplitudeChanged -> {
                            val currentState = _state.value
                            if (currentState is RecordState.Recording) {
                                _state.value = currentState.copy(amplitudeLevel = event.level)
                            }
                        }

                        is RecordEvent.MaxDurationReached -> {
                            _state.value = RecordState.Stopped(event.record)
                        }
                    }
                }
            }
        }
    }

    fun onRecordingStarted() {
        _state.value = RecordState.Recording(durationMs = 0L, amplitudeLevel = 0f)
    }

    fun onRecordingStopped(record: AudioRecord) {
        _state.value = RecordState.Stopped(record = record)
    }

    fun onRecordingError(message: String) {
        _state.value = RecordState.Error(message = message)
    }

    fun updateDuration(durationMs: Long) {
        val currentState = _state.value
        if (currentState is RecordState.Recording) {
            _state.value = currentState.copy(durationMs = durationMs)
        }
    }
}