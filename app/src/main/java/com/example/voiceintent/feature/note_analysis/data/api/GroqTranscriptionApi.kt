package com.example.voiceintent.feature.note_analysis.data.api

import com.example.voiceintent.feature.note_analysis.data.api.model.TranscriptionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GroqTranscriptionApi {
    @Multipart
    @POST("audio/transcriptions")
    suspend fun transcribe(
        @Part file: MultipartBody.Part,
        @Part model: MultipartBody.Part,
        @Part responseFormat: MultipartBody.Part,
        @Part language: MultipartBody.Part?
    ): TranscriptionResponse
}