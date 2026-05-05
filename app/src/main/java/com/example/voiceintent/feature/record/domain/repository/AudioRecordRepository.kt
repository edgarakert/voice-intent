package com.example.voiceintent.feature.record.domain.repository

import com.example.voiceintent.feature.record.domain.entity.AudioRecord

interface AudioRecordRepository {
    fun set(record: AudioRecord)

    fun get(): AudioRecord?

    fun remove()
}