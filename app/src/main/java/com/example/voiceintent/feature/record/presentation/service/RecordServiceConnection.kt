package com.example.voiceintent.feature.record.presentation.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import kotlinx.coroutines.flow.SharedFlow

class RecordServiceConnection(private val service: RecordService) : RecordControl {
    override val eventsFlow: SharedFlow<RecordEvent>
        get() = service.eventsFlow

    @RequiresApi(Build.VERSION_CODES.S)
    override fun start(language: AudioLanguage) {
        service.start(language = language)
    }

    override fun stop(): AudioRecord = service.stop()
}