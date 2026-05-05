package com.example.voiceintent.feature.record.data.data_source

import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import com.example.voiceintent.feature.record.domain.repository.AudioRecordRepository
import javax.inject.Inject

class AudioRecordInMemoryDataSource @Inject constructor() : AudioRecordRepository {
    private var audioRecord: AudioRecord? = null

    override fun set(record: AudioRecord) {
        audioRecord = record
    }

    override fun get(): AudioRecord? = audioRecord

    override fun remove() {
        audioRecord = null
    }
}