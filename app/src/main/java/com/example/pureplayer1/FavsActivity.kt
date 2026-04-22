package com.example.pureplayer1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pureplayer1.data.Track
import kotlin.collections.List

class FavsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

//data class Track(
//    val id: String,
//    val title: String,
//    val artist: String,
//    val duration: String,
//    val isLiked: Boolean = true
//)

// Экран избранного
@Composable
fun FavoritesScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onUnlikeClick: () -> Unit = {},
    navController: NavHostController,
) {
//    // Состояние с лайкнутыми треками
//    val likedTracks = rememberLikedTracks()

    val navBackStateEntry by navController.currentBackStackEntryAsState()
    var currentRoute = navBackStateEntry?.destination?.route


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .padding(horizontal = 16.dp)
        ) {
            // Заголовок
            FavoritesHeader()

//            Spacer(modifier = Modifier.height(16.dp))

//            // Список треков или пустое состояние
//            if (likedTracks.isEmpty()) {
//                EmptyFavoritesState()
//            } else {
//                FavoritesList(
//                    tracks = likedTracks,
//                    onUnlikeClick = {},
//                    onTrackClick = { onTrackClick(track) },
//                    onLikeClick = {},
//                )
//            }
            TrackList(
                tracks = listOf(
                    Track(
//                        id = "1",
                        title = "Blinding Lights",
                        artist = "The Weeknd",
//                        duration = "3:20",
                        audioUrl = "",
                        coverUrl = "",
//                        isLiked = true,
//                        playCount = 2500000
                    ),
//                    Track(
//                        id = "7",
//                        title = "Good 4 U",
//                        artist = "Olivia Rodrigo",
//                        duration = "2:58",
//                        isLiked = true,
//                        playCount = 1500000
//                    ),
//                    Track(
//                        id = "9",
//                        title = "Shivers",
//                        artist = "Ed Sheeran",
//                        duration = "3:27",
//                        isLiked = true,
//                        playCount = 1300000
//                    ),
//                    Track(
//                        id = "10",
//                        title = "Easy On Me",
//                        artist = "Adele",
//                        duration = "3:44",
//                        isLiked = false,
//                        playCount = 1700000
//                    )
                ),
                onTrackClick = { track ->
                    // TODO: Обработка клика на трек
                },
                navController = navController,
                onLikeClick = { track ->
                    // TODO: Отправка запроса на сервер для добавления/удаления из избранного
                }
            )
        }

    }


// Заголовок экрана
@Composable
fun FavoritesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Избранное",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// Список избранных треков
//@Composable
//fun FavoritesList(
//    tracks: List<Track>,
//    onTrackClick: (Track) -> Unit,
//    onLikeClick: (Track) -> Unit,
//    onUnlikeClick: (Track) -> Unit
//) {
//
//    TrackList(
//        tracks = listOf(
//            Track(
//                id = "1",
//                title = "Blinding Lights",
//                artist = "The Weeknd",
//                duration = "3:20",
//                isLiked = true,
//                playCount = 2500000
//            ),
//            Track(
//                id = "2",
//                title = "Stay",
//                artist = "The Kid LAROI, Justin Bieber",
//                duration = "2:21",
//                isLiked = false,
//                playCount = 1800000
//            ),
//            Track(
//                id = "3",
//                title = "Heat Waves",
//                artist = "Glass Animals",
//                duration = "3:58",
//                isLiked = true,
//                playCount = 2200000
//            ),
//            Track(
//                id = "4",
//                title = "As It Was",
//                artist = "Harry Styles",
//                duration = "2:47",
//                isLiked = false,
//                playCount = 1900000
//            ),
//            Track(
//                id = "5",
//                title = "Bad Habit",
//                artist = "Steve Lacy",
//                duration = "3:52",
//                isLiked = true,
//                playCount = 1600000
//            )
//        ),
//        onTrackClick = { track ->
//            // TODO: Обработка клика на трек
//            // Например: navigation.navigate("player/${track.id}")
//            // Или: viewModel.playTrack(track)
//        },
//        navController = navController,
//        onLikeClick = { track ->
//            // TODO: Отправка запроса на сервер для добавления/удаления из избранного
//            // Например: viewModel.toggleLike(track.id)
//        }
//    )
//    }


// Элемент избранного трека
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTrackItem(
    track: Track,
    onUnlikeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Плейсхолдер обложки
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Обложка",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Информация о треке
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                track.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                track.artist?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            // Длительность
//            Text(
////                text = track.duration,
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.outline,
//                modifier = Modifier.padding(end = 8.dp)
//            )

            // Кнопка "Убрать из избранного"
            IconButton(
                onClick = onUnlikeClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Убрать из избранного",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Пустое состояние
@Composable
fun EmptyFavoritesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "В избранном пока пусто",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Добавляйте понравившиеся треки,\nчтобы они появились здесь",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Нижняя навигация
@Composable
fun FavoritesBottomNavigation(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    NavigationBar(
        modifier = Modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Главная"
                )
            },
            label = {
                Text(
                    text = "Главная",
                    fontSize = 12.sp
                )
            },
            selected = false,
            onClick = onHomeClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск"
                )
            },
            label = {
                Text(
                    text = "Поиск",
                    fontSize = 12.sp
                )
            },
            selected = false,
            onClick = onSearchClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Избранное"
                )
            },
            label = {
                Text(
                    text = "Избранное",
                    fontSize = 12.sp
                )
            },
            selected = true,
            onClick = onFavoritesClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

// Временное состояние для примера
//@Composable
//fun rememberLikedTracks(): List<Track> {
//    return remember {
//        listOf(
//            Track("1", "Blinding Lights", "The Weeknd", "3:20"),
//            Track("2", "Stay", "The Kid LAROI, Justin Bieber", "2:21"),
//            Track("3", "As It Was", "Harry Styles", "2:47"),
//            Track("4", "Levitating", "Dua Lipa", "3:23"),
//            Track("5", "Heat Waves", "Glass Animals", "3:58"),
//            Track("6", "Flowers", "Miley Cyrus", "3:20"),
//
//        )
//    }
//}
