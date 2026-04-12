package com.example.voiceintent.feature.record.presentation.service

import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import kotlinx.coroutines.flow.SharedFlow

interface RecordControl {
    val eventsFlow: SharedFlow<RecordEvent>

    fun start(language: AudioLanguage)

    fun stop(): AudioRecord
}