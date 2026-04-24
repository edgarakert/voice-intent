package com.example.voiceintent.feature.record.domain.entity

enum class AudioLanguage(val code: String) {
    Russian(code = "ru"),
    English(code = "en"),
    Armenian(code = "hy"),
    Auto(code = "");

    companion object {
        fun fromCode(code: String): AudioLanguage =
            entries.find { it.code == code } ?: Auto
    }
}