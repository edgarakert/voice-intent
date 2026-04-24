package com.example.voiceintent.feature.record.presentation.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.example.voiceintent.feature.record.domain.entity.AudioLanguage
import com.example.voiceintent.feature.record.domain.entity.AudioRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File

class RecordService : Service() {
    inner class LocalBinder : Binder() {
        fun getService(): RecordService = this@RecordService
    }

    private val binder = LocalBinder()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var startedAt: Long = 0L
    private var amplitudeJob: Job? = null

    private var language: AudioLanguage = AudioLanguage.Auto

    private val _eventsFlow = MutableSharedFlow<RecordEvent>()
    val eventsFlow: SharedFlow<RecordEvent> = _eventsFlow.asSharedFlow()

    override fun onBind(intent: Intent?): IBinder = binder

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.S)
    fun start(language: AudioLanguage) {
        this.language = language
        outputFile = createOutputFile()
        startedAt = System.currentTimeMillis()

        recorder = MediaRecorder(this).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(44100)
            setAudioEncodingBitRate(128_000)
            setMaxDuration(5 * 60 * 1000)
            setOnInfoListener { _, status, _ ->
                if (status == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    val record = this@RecordService.stop()
                    coroutineScope.launch {
                        _eventsFlow.emit(RecordEvent.MaxDurationReached(record = record))
                    }
                }

            }
            setOutputFile(outputFile)
            prepare()
            start()
        }

        startForeground(NOTIFICATION_ID, buildNotification())
        startAmplitudePolling()
    }

    private fun createOutputFile(): File {
        val directory = File(filesDir, "recordings").also { it.mkdirs() }
        return File(directory, "record_${System.currentTimeMillis()}.m4a")
    }


    fun stop(): AudioRecord {
        amplitudeJob?.cancel()

        recorder?.apply {
            stop()
            release()
        }
        recorder = null

        stopForeground(STOP_FOREGROUND_REMOVE)

        val file = checkNotNull(outputFile) {
            "output is null on stop"
        }
        val durationMs = System.currentTimeMillis() - startedAt

        return AudioRecord(
            path = file.absolutePath,
            durationMs = durationMs
        )
    }

    private fun startAmplitudePolling() {
        amplitudeJob = coroutineScope.launch {
            while (true) {
                delay(100)
                val raw = recorder?.maxAmplitude ?: 0
                val normalized = (raw / 32767f).coerceIn(0f, 1f)
                _eventsFlow.emit(RecordEvent.AmplitudeChanged(level = normalized))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Запись голоса",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Активна во время записи"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(): Notification =
        Notification.Builder(this, CHANNEL_ID).setContentTitle("Идёт запись...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now).setOngoing(true).build()

    override fun onDestroy() {
        super.onDestroy()
        amplitudeJob?.cancel()
        amplitudeJob = null
        coroutineScope.cancel()
        recorder?.release()
        recorder = null
    }

    companion object {
        private const val CHANNEL_ID = "record_channel"
        private const val NOTIFICATION_ID = 1
    }
}