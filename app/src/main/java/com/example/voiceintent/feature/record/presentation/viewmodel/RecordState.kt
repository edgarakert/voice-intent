package com.example.voiceintent.feature.record.presentation.viewmodel

import com.example.voiceintent.feature.record.domain.entity.AudioRecord

sealed class RecordState {
    object Idle : RecordState()

    data class Recording(val durationMs: Long, val amplitudeLevel: Float) : RecordState()

    data class Stopped(val record: AudioRecord) : RecordState()

    data class Error(val message: String) : RecordState()
}
