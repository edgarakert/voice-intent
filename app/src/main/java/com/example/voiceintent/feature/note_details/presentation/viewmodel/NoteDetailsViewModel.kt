package com.example.voiceintent.feature.note_details.presentation.viewmodel

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.feature.note_details.domain.use_case.DeleteNoteUseCase
import com.example.voiceintent.feature.note_details.domain.use_case.GetNoteDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    application: Application,
    private val getNoteDetailsUseCase: GetNoteDetailsUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    private val _state = MutableStateFlow<NoteDetailsState>(NoteDetailsState.Loading)
    val state: StateFlow<NoteDetailsState> = _state.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            val current = _state.value

            try {
                val note = getNoteDetailsUseCase.invoke(id = noteId)

                val playerState = if (current is NoteDetailsState.Success)
                    current.playerState
                else PlayerState.Idle

                _state.value = NoteDetailsState.Success(
                    note = note,
                    playerState = playerState
                )

            } catch (_: Exception) {
                _state.value = NoteDetailsState.Error(message = "Не удалось найти заметку")
            }
        }
    }

    fun playAudio() {
        val current = _state.value

        if (current !is NoteDetailsState.Success) {
            return
        }

        val note = current.note
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(note.audioPath)
                    prepare()
                    setOnCompletionListener { viewModelScope.launch { onPlaybackCompleted() } }
                }
            } catch (_: Exception) {
                return
            }
        }
        mediaPlayer?.start()
        startProgressTracking()
        updatePlayerState(PlayerState.Playing)
    }

    fun pauseAudio() {
        val player = mediaPlayer ?: return
        player.pause()
        progressJob?.cancel()
        val currentMs = player.currentPosition.toLong()
        val totalMs = player.duration.toLong()
        updatePlayerState(PlayerState.Paused(currentMs = currentMs, totalMs = totalMs))
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.seekTo(positionMs.toInt())
    }

    fun deleteNote() {
        val note = (_state.value as? NoteDetailsState.Success)?.note ?: return
        viewModelScope.launch {
            deleteNoteUseCase(note)
            _state.value = NoteDetailsState.Deleted
        }
    }

    private fun onPlaybackCompleted() {
        progressJob?.cancel()
        mediaPlayer?.seekTo(0)
        updatePlayerState(PlayerState.Idle)
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                val player = mediaPlayer ?: break
                val currentMs = player.currentPosition.toLong()
                val totalMs = player.duration.toLong()
                updatePlayerState(PlayerState.Progress(currentMs = currentMs, totalMs = totalMs))
                delay(500)
            }
        }
    }

    private fun updatePlayerState(playerState: PlayerState) {
        val current = _state.value
        if (current is NoteDetailsState.Success) {
            _state.value = current.copy(playerState = playerState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}