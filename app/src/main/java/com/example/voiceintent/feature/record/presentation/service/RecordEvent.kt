package com.example.voiceintent.feature.record.presentation.service

import com.example.voiceintent.feature.record.domain.entity.AudioRecord

sealed class RecordEvent {
    data class AmplitudeChanged(val level: Float) : RecordEvent()
    data class MaxDurationReached(val record: AudioRecord) : RecordEvent()
}