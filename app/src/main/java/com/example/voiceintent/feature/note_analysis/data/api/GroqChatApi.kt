package com.example.voiceintent.feature.note_analysis.data.api

import com.example.voiceintent.feature.note_analysis.data.api.model.ChatRequest
import com.example.voiceintent.feature.note_analysis.data.api.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GroqChatApi {
    @POST("chat/completions")
    suspend fun analyze(
        @Body request: ChatRequest
    ): ChatResponse
}