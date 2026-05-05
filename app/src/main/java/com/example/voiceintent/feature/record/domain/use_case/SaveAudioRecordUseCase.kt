package com.example.voiceintent.feature.record.domain.use_case

import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import com.example.voiceintent.feature.record.domain.repository.AudioRecordRepository
import javax.inject.Inject

class SaveAudioRecordUseCase @Inject constructor(
    private val repository: AudioRecordRepository
) {
    operator fun invoke(record: AudioRecord) = repository.set(record = record)
}