package com.example.pureplayer1

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pureplayer1.data.Track
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist


object PlayerState {
    var currentTrack by mutableStateOf<Track?>(null)
    var isPlaying by mutableStateOf(false)
    var isVisible by mutableStateOf(false)
    var repeatMode by mutableStateOf(RepeatMode.OFF)

    // Очередь треков для переключения
    var currentPlaylist by mutableStateOf<List<Track>>(emptyList())
    var currentPlaylistType by mutableStateOf(PlaylistType.NONE)

    lateinit var audioPlayer: AudioPlayerManager
    lateinit var favoritesManager: FavoritesManager
        private set

    fun init(context: Context) {
        if (!::audioPlayer.isInitialized) {
            audioPlayer = AudioPlayerManager(context.applicationContext)
        }
        if (!::favoritesManager.isInitialized) {
            favoritesManager = FavoritesManager(context.applicationContext)
        }
    }

    fun playTrack(track: Track, playlist: List<Track> = emptyList(), playlistType: PlaylistType = PlaylistType.NONE) {
        currentTrack = track
        if (playlist.isNotEmpty()) {
            currentPlaylist = playlist
            currentPlaylistType = playlistType
        }
        isPlaying = true
        isVisible = true
        audioPlayer.playTrack(track)
    }

    fun playNext() {
        if (currentPlaylist.isEmpty()) return

        val currentIndex = currentPlaylist.indexOfFirst {
            it.title == currentTrack?.title && it.artist == currentTrack?.artist
        }

        if (currentIndex != -1 && currentIndex + 1 < currentPlaylist.size) {
            val nextTrack = currentPlaylist[currentIndex + 1]
            playTrack(nextTrack, currentPlaylist, currentPlaylistType)
        } else if (repeatMode == RepeatMode.ALL && currentPlaylist.isNotEmpty()) {
            // Зацикливание плейлиста
            val firstTrack = currentPlaylist.first()
            playTrack(firstTrack, currentPlaylist, currentPlaylistType)
        }
    }

    fun playPrevious() {
        if (currentPlaylist.isEmpty()) return

        val currentIndex = currentPlaylist.indexOfFirst {
            it.title == currentTrack?.title && it.artist == currentTrack?.artist
        }

        if (currentIndex > 0) {
            val previousTrack = currentPlaylist[currentIndex - 1]
            playTrack(previousTrack, currentPlaylist, currentPlaylistType)
        } else if (repeatMode == RepeatMode.ALL && currentPlaylist.isNotEmpty()) {
            // Зацикливание плейлиста
            val lastTrack = currentPlaylist.last()
            playTrack(lastTrack, currentPlaylist, currentPlaylistType)
        }
    }

    fun togglePlay() {
        audioPlayer.togglePlay()
    }

    fun hide() {
        isVisible = false
    }

    fun seekTo(position: Int) {
        audioPlayer.seekTo(position)
    }

    fun toggleRepeat() {
        repeatMode = when (repeatMode) {
            RepeatMode.OFF -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.OFF
        }
    }

    fun isTrackFavorite(track: Track?): Boolean {
        track ?: return false
        return favoritesManager.isFavorite(track)
    }

    fun toggleFavorite(track: Track?): Boolean {
        track ?: return false
        return favoritesManager.toggleFavorite(track)
    }

    fun release() {
        if (::audioPlayer.isInitialized) {
            audioPlayer.release()
        }
    }
}

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class PlaylistType {
    NONE, SEARCH, FAVORITES
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    navController: NavHostController,
    track: Track
) {
    val isPlaying by PlayerState.audioPlayer.isPlaying.collectAsStateWithLifecycle()
    val currentPosition by PlayerState.audioPlayer.currentPosition.collectAsStateWithLifecycle()
    val duration by PlayerState.audioPlayer.duration.collectAsStateWithLifecycle()
    val isPreparing by PlayerState.audioPlayer.isPreparing.collectAsStateWithLifecycle()
    val error by PlayerState.audioPlayer.error.collectAsStateWithLifecycle()
    val repeatMode by remember { derivedStateOf { PlayerState.repeatMode } }

    // Состояние лайка
    var isLiked by remember { mutableStateOf(PlayerState.isTrackFavorite(track)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Сейчас играет",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Обложка
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (!track.coverUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = track.coverUrl,
                        contentDescription = "Обложка",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Информация о треке
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = track.title ?: "Без названия",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = track.artist ?: "Неизвестный исполнитель",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Прогресс бар
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (duration > 0) {
                    Slider(
                        value = currentPosition.toFloat(),
                        onValueChange = { newValue ->
                            PlayerState.seekTo(newValue.toInt())
                        },
                        valueRange = 0f..duration.toFloat(),
                        enabled = !isPreparing,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatTime(duration),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Предыдущий трек */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Предыдущий",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (!isPreparing) {
                            PlayerState.togglePlay()
                        }
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPreparing)
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            else
                                MaterialTheme.colorScheme.primary
                        ),
                    enabled = !isPreparing
                ) {
                    if (isPreparing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Пауза" else "Воспроизвести",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                IconButton(
                    onClick = { /* Следующий трек */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Следующий",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Нижняя панель с кнопками
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Кнопка повтора
                IconButton(
                    onClick = { PlayerState.toggleRepeat() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = when (repeatMode) {
                            RepeatMode.OFF -> Icons.Default.Repeat
                            RepeatMode.ONE -> Icons.Default.RepeatOne
                            RepeatMode.ALL -> Icons.Default.Repeat
                        },
                        contentDescription = when (repeatMode) {
                            RepeatMode.OFF -> "Без повтора"
                            RepeatMode.ONE -> "Повтор трека"
                            RepeatMode.ALL -> "Повтор плейлиста"
                        },
                        tint = when (repeatMode) {
                            RepeatMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant
                            else -> MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Кнопка лайка
                IconButton(
                    onClick = {
                        isLiked = PlayerState.toggleFavorite(track)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isLiked) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun formatTime(milliseconds: Int): String {
    if (milliseconds <= 0) return "0:00"
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}