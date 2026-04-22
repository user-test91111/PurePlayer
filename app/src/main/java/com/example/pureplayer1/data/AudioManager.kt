package com.example.pureplayer1

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import com.example.pureplayer1.data.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrack: Track? = null
    private var updateJob: kotlinx.coroutines.Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private val _isPreparing = MutableStateFlow(false)
    val isPreparing: StateFlow<Boolean> = _isPreparing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun playTrack(track: Track) {
        // Если тот же трек и играет, ставим на паузу
        if (currentTrack == track && mediaPlayer?.isPlaying == true) {
            pause()
            return
        }

        // Если тот же трек и на паузе, возобновляем
        if (currentTrack == track && mediaPlayer != null && !_isPlaying.value) {
            resume()
            return
        }

        currentTrack = track
        val audioUrl = track.audioUrl

        if (audioUrl.isNullOrEmpty()) {
            _error.value = "Нет доступного аудио для этого трека"
            return
        }

        stopUpdater()
        _isPreparing.value = true
        _error.value = null
        _currentPosition.value = 0
        _duration.value = 0

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(audioUrl)
                prepareAsync()

                setOnPreparedListener {
                    _isPreparing.value = false
                    start()
                    _isPlaying.value = true
                    _duration.value = this.duration
                    startUpdater()
                    Log.d("AudioPlayer", "Track started: ${track.title}, duration: ${this.duration}")
                }

                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0
                    stopUpdater()
                    Log.d("AudioPlayer", "Track completed")
                }

                setOnErrorListener { _, what, extra ->
                    _isPreparing.value = false
                    _error.value = "Ошибка воспроизведения: $what, $extra"
                    stopUpdater()
                    Log.e("AudioPlayer", "Error: what=$what, extra=$extra")
                    true
                }
            }
        } catch (e: Exception) {
            _isPreparing.value = false
            _error.value = "Ошибка: ${e.message}"
            Log.e("AudioPlayer", "Error playing track", e)
        }
    }

    private fun startUpdater() {
        stopUpdater()
        updateJob = scope.launch {
            while (_isPlaying.value && mediaPlayer?.isPlaying == true) {
                delay(500)
                _currentPosition.value = mediaPlayer?.currentPosition ?: 0
            }
        }
    }

    private fun stopUpdater() {
        updateJob?.cancel()
        updateJob = null
    }

    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.value = false
        stopUpdater()
        Log.d("AudioPlayer", "Paused at position: ${_currentPosition.value}")
    }

    fun resume() {
        if (mediaPlayer != null) {
            mediaPlayer?.start()
            _isPlaying.value = true
            startUpdater()
            Log.d("AudioPlayer", "Resumed from position: ${_currentPosition.value}")
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _currentPosition.value = position
    }

    fun togglePlay() {
        if (_isPreparing.value) {
            Log.d("AudioPlayer", "Still preparing, ignoring toggle")
            return
        }

        if (_isPlaying.value) {
            pause()
        } else {
            if (mediaPlayer != null) {
                resume()
            } else {
                currentTrack?.let { playTrack(it) }
            }
        }
    }

    fun stop() {
        stopUpdater()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _isPreparing.value = false
    }

    fun release() {
        stopUpdater()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}