package com.example.voiceintent.feature.note_analysis.data.repository

import com.example.voiceintent.di.IODispatcher
import com.example.voiceintent.feature.note_analysis.data.api.GroqChatApi
import com.example.voiceintent.feature.note_analysis.data.api.GroqTranscriptionApi
import com.example.voiceintent.feature.note_analysis.data.api.extension.toMood
import com.example.voiceintent.feature.note_analysis.data.api.model.ChatMessage
import com.example.voiceintent.feature.note_analysis.data.api.model.ChatRequest
import com.example.voiceintent.feature.note_analysis.data.api.model.NoteAnalysis
import com.example.voiceintent.feature.note_analysis.domain.entity.NoteAnalysisResult
import com.example.voiceintent.feature.note_analysis.domain.repository.NoteAnalysisRepository
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import javax.inject.Inject

class NoteAnalysisRepositoryImpl @Inject constructor(
    private val transcriptionApi: GroqTranscriptionApi,
    private val chatApi: GroqChatApi,
    private val json: Json,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : NoteAnalysisRepository {
    override suspend fun transcribe(
        audioFile: File,
        language: AudioLanguage
    ): String = withContext(ioDispatcher) {
        val fileBytes = audioFile.readBytes()


        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = audioFile.name,
            body = object : RequestBody() {
                override fun contentType() = "audio/m4a".toMediaType()

                override fun writeTo(sink: BufferedSink) {
                    sink.write(audioFile.readBytes())
                }

                override fun contentLength(): Long = fileBytes.size.toLong()
            }
        )

        val modelPart = MultipartBody.Part.createFormData("model", "whisper-large-v3-turbo")

        val responseFormatPart = MultipartBody.Part.createFormData("response_format", "json")

        val languagePart = if (language != AudioLanguage.Auto) {
            MultipartBody.Part.createFormData("language", language.code)
        } else null


        return@withContext transcriptionApi.transcribe(
            file = filePart,
            model = modelPart,
            responseFormat = responseFormatPart,
            language = languagePart
        ).text
    }

    override suspend fun analyze(transcript: String): NoteAnalysisResult =
        withContext(ioDispatcher) {
            val request = ChatRequest(
                model = "llama-3.3-70b-versatile",
                messages = listOf(
                    ChatMessage(role = "system", content = CHAT_SYSTEM_PROMPT),
                    ChatMessage(role = "user", content = transcript)
                )
            )

            val rawContent = chatApi.analyze(request).choices.first().message.content

            val dto = json.decodeFromString<NoteAnalysis>(rawContent)

            return@withContext NoteAnalysisResult(
                transcript = transcript,
                tags = dto.tags,
                tasks = dto.tasks,
                mood = dto.mood.toMood(),
                summary = dto.summary
            )
        }

    companion object {
        private val CHAT_SYSTEM_PROMPT = """
    You are a personal diary assistant. Analyze the transcribed voice note and respond ONLY with a valid JSON object — no markdown, no explanation, no extra text.
    
    IMPORTANT: All text fields (tags, summary, tasks) MUST be written in the same language as the input text. If the input is in Russian, respond in Russian. If in English, respond in English.

    The JSON must have exactly these fields:
    {
      "tags": string[],
      "tasks": string[],
      "mood": "positive" | "neutral" | "negative",
      "summary": string
    }

    - tags: 2–5 semantic tags. MUST be in the same language as the transcribed text
    - tasks: action items mentioned (empty array if none). MUST be in the same language as the transcribed text
    - summary: 1–2 sentences. MUST be in the same language as the transcribed text
    - mood: ONLY one of these exact values: "positive", "neutral", "negative"
""".trimIndent()
    }
}