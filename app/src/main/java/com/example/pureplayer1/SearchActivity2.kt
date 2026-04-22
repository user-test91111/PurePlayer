//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Clear
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.pureplayer1.TrackItem
//import com.example.pureplayer1.bottomBarApp
//import kotlinx.coroutines.delay
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.pureplayer1.HeaderSection
//import com.example.pureplayer1.MiniPlayer
//import com.example.pureplayer1.Navigate
//import com.example.pureplayer1.PlayerState
//import com.example.pureplayer1.Routes
//import com.example.pureplayer1.SectionTitle
//import com.example.pureplayer1.TrackList
//import com.example.pureplayer1.data.Track
//import com.example.pureplayer1.data.fetchMusic
//import com.example.pureplayer1.model.AudioViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchScreen(
//    navController: NavHostController,
//    viewModel: AudioViewModel
//) {
//    var query by remember { mutableStateOf("") }
//    val searchTracks by viewModel.searchTracks.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//    val navBackStateEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStateEntry?.destination?.route
//
//    LaunchedEffect(query) {
//        if (query.isNotEmpty()) {
//            delay(500)
//            viewModel.searchBySearchBar(query)
//        } else {
//            viewModel.clearSearch()
//        }
//    }
//
//    Scaffold(
//        bottomBar = {
//            Column {
//                MiniPlayer(navController, currentRoute, Modifier.padding(bottom = 4.dp))
//                bottomBarApp(navController, currentRoute)
//            }
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(paddingValues)
//        ) {
//            SearchBar(
//                query = query,
//                onQueryChange = { query = it },
//                onClearClick = {
//                    query = ""
//                    viewModel.clearSearch()
//                },
//                modifier = Modifier.padding(16.dp)
//            )
//
//            when {
//                isLoading -> LoadingIndicator()
//                error != null -> ErrorState(error, query, viewModel)
//                searchTracks.isNotEmpty() -> TrackList(
//                    tracks = searchTracks,
//                    onTrackClick = { track ->
//                        PlayerState.playTrack(track)
//                        navController.navigate("player/${track.id}")
//                    },
//                    onLikeClick = { /* Обработка лайка */ },
//                    navController = navController
//                )
//                query.isNotEmpty() -> EmptyState(query)
//                else -> EmptyQueryState()
//            }
//        }
//    }
//}
//
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(MaterialTheme.colorScheme.background)
////        ) {
////            // Поисковая строка
////            SearchBar(
////                query = query,
////                onQueryChange = { text ->
////                    query = text
////                },
////                onClearClick = {
////                    query = ""
////                    viewModel.clearSearch()
////                    navController.navigate(Routes.Search.route)
////                },
////                modifier = Modifier.padding(36.dp),
////            )
////
////            // Отображение состояния
////            when {
////                isLoading -> {
////                    Box(
////                        modifier = Modifier.fillMaxSize(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        CircularProgressIndicator()
////                    }
////                }
////
////                error != null -> {
////                    Box(
////                        modifier = Modifier.fillMaxSize(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        Column(
////                            horizontalAlignment = Alignment.CenterHorizontally
////                        ) {
////                            Text(
////                                text = "Ошибка: $error",
////                                color = MaterialTheme.colorScheme.error,
////                                modifier = Modifier.padding(16.dp)
////                            )
////                            Button(
////                                onClick = { viewModel.searchBySearchBar(query) }
////                            ) {
////                                Text("Повторить")
////                            }
////                        }
////                    }
////                }
////
////                searchTracks.isEmpty() && query.isNotEmpty() -> {
////                    Box(
////                        modifier = Modifier.fillMaxSize(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        Text("Ничего не найдено")
////                    }
////                }
////
////                searchTracks.isNotEmpty() -> {
////                    TrackList(
////                        tracks = searchTracks,
////                        onTrackClick = { track ->
////                            // Запускаем трек
//////                        PlayerState.playTrack(track)
//////                        navController.navigate("player/${track.id}")
////                        },
////                        onLikeClick = { track ->
////                            // Обработка лайка
////                            // Здесь можно обновить состояние лайка на сервере
////                        },
////                        navController = navController
////                    )
////                }
////
////                query.isEmpty() -> {
////                    Box(
////                        modifier = Modifier.fillMaxSize(),
////                        contentAlignment = Alignment.Center
////                    ) {
////                        Text("Введите название трека или исполнителя")
////                    }
////                }
////            }
////
////        }
////    }
//
//
//
//
////@OptIn(ExperimentalMaterial3Api::class)
////@Composable
////fun SearchScreen(navController: NavHostController,viewModel: AudioViewModel) {
////    // Получаем состояние поиска
//////    val ( results, isLoading, updateQuery, clearQuery) = rememberSearchState()
////    var query by remember { mutableStateOf("") }
////    val results by remember { mutableStateOf("") }
////    val isLoading by remember { mutableStateOf("") }
////    val updateQuery by remember { mutableStateOf("") }
////    val clearQuery by remember { mutableStateOf("") }
////
////    // Вычисляем isEmpty
////    val isEmpty = results.isEmpty() && query.isNotEmpty()
////    val navBackStateEntry by navController.currentBackStackEntryAsState()
////    var currentRoute = navBackStateEntry?.destination?.route
////    Scaffold(
////        bottomBar = {Column {
////            // Мини-плеер (показывается над bottom bar)
////            MiniPlayer(
////                navController = navController,
////                currentRoute = currentRoute,
////                modifier = Modifier.padding(bottom = 4.dp)
////            )
////
////            // Обычный bottom bar
////            bottomBarApp(navController, currentRoute = currentRoute)
////        }}
////
////    ) { paddingValues ->
////
////        Column(
////            modifier = Modifier
////                .fillMaxSize()
////                .padding(paddingValues)
////        ) {
////            // Поисковая строка
////            SearchBar(
////                query = query,
////                onQueryChange = {text ->
////                    query = text
////                },
////                onClearClick = {navController.navigate(Routes.Search.route)},
////                modifier = Modifier.padding(16.dp),
////            )
////
////            viewModel.searchBySearchBar(query)
////            TrackList(
////                tracks = viewModel.searchTracks as List<Track>,
////                onTrackClick = { track ->
////                },
////                navController = navController,
////                onLikeClick = { track ->
////
////                })
////        }
////    }
////}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    onClearClick: @Composable () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = MaterialTheme.shapes.large,
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        TextField(
//            value = query,
//            onValueChange = onQueryChange,
//            modifier = Modifier.fillMaxWidth(),
//            placeholder = {
//                Text(
//                    text = "Поиск треков...",
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Поиск",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            },
//            trailingIcon = {
//                if (query.isNotEmpty()) {
//                    IconButton(onClick = onClearClick) { // <- просто передаем функцию
//                        Icon(
//                            imageVector = Icons.Default.Clear,
//                            contentDescription = "Очистить",
//                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    }
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun TrackListContent(
//    tracks: List<Track>,
//    isEmpty: Boolean,
//    query: String,
//    onTrackClick: (Track) -> Unit,
//    onLikeClick: (Track) -> Unit
//) {
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        if (isEmpty) {
//            EmptyState(query = query)
//        } else {
//            LazyColumn(
//                state = rememberLazyListState(),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                items(tracks) { track ->
//                    TrackItem(
//                        track = track,
//                        onTrackClick = { onTrackClick(track) },
//                        onLikeClick = { onLikeClick(track) },
//                        navController = rememberNavController(),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 4.dp)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmptyState(query: String) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Ничего не найдено",
//                tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//            Text(
//                text = if (query.isNotEmpty()) "Ничего не найдено" else "Начните поиск",
//                style = MaterialTheme.typography.titleMedium,
//                color = MaterialTheme.colorScheme.onSurface,
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = if (query.isNotEmpty()) {
//                    "По запросу \"$query\" ничего не найдено"
//                } else {
//                    "Введите название трека, исполнителя или альбома"
//                },
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun LoadingIndicator() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        androidx.compose.material3.CircularProgressIndicator(
//            color = MaterialTheme.colorScheme.primary
//        )
//    }
//}
//
//// Состояние поиска
//class SearchState(
//    val query: String,
//    val results: List<Track>,
//    val isLoading: Boolean,
//    val isEmpty: Boolean,
//    val updateQuery: (String) -> Unit,
//    val clearQuery: () -> Unit,
//    val onTrackClick: (Track) -> Unit,
//    val onLikeClick: (Track) -> Unit
//)
//
//@Composable
//fun rememberSearchState(): SearchState {
//    var query by remember { mutableStateOf("") }
//    var results by remember { mutableStateOf<List<Track>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(false) }
//
//    // Автоматический поиск при изменении запроса
//    LaunchedEffect(query) {
//        if (query.length >= 2) {
//            isLoading = true
//            delay(300) // Debounce
//            results = performSearch(query)
//            isLoading = false
//        } else {
//            results = emptyList()
//        }
//    }
//
//    return SearchState(
//        query = query,
//        results = results,
//        isLoading = isLoading,
//        isEmpty = results.isEmpty() && query.isNotEmpty() && !isLoading,
//        updateQuery = { newQuery ->
//            query = newQuery
//        },
//        clearQuery = {
//            query = ""
//            results = emptyList()
//        },
//        onTrackClick = { track ->
//            // TODO: Обработка клика на трек
//        },
//        onLikeClick = { track ->
//            // TODO: Обработка лайка трека
//        }
//    )
//}
////
//// Вспомогательные функции
//fun performSearch(query: String): List<Track> {
//    // TODO: Заменить на реальный API вызов
//    return when {
//        query.contains("weeknd", ignoreCase = true) -> listOf(
//            Track(
////                id = "1",
//                title = "Blinding Lights",
//                artist = "The Weeknd",
////                duration = "3:20",
//                audioUrl = "",
//                coverUrl = "",
////                isLiked = true,
////                playCount = 2500000
//            )
//        )
//        else -> emptyList()
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SearchBarPreview() {
//    MaterialTheme {
//        SearchBar(
//            query = "The Weeknd",
//            onQueryChange = {},
//            onClearClick = {}
//        )
//    }
//}

package com.example.pureplayer1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.pureplayer1.data.Track
import com.example.pureplayer1.model.AudioViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: AudioViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }

    val searchTracks by viewModel.searchTracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Поиск с задержкой
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            delay(500)
            viewModel.searchBySearchBar(query)
        } else {
            viewModel.clearSearch()
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding()
        ) {
            // Поисковая строка
            SearchBarComponent(
                query = query,
                onQueryChange = { query = it },
                onClearClick = {
                    query = ""
                    viewModel.clearSearch()
                },
                modifier = Modifier.padding(16.dp)
            )

            // Контент
            when {
                isLoading -> LoadingContent()
                error != null -> ErrorContent(error = error, onRetry = {
                    viewModel.searchBySearchBar(query)
                })
                searchTracks.isNotEmpty() -> TrackListContent(
                    tracks = searchTracks,
                    navController = navController
                )
                query.isNotEmpty() -> EmptyResultContent(query)
                else -> EmptySearchContent()
            }
        }
    }


// Поисковая строка
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Поиск треков...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

// Список треков
@Composable
fun TrackListContent(
    tracks: List<Track>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(tracks) { track ->
            TrackItem(
                track = track,
                onTrackClick = {
//                    PlayerState.playTrack(track)
//                    navController.navigate("player/${track.id}")
                },
                onLikeClick = { /* Обработка лайка */ },
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

// Состояние загрузки
@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Состояние ошибки
@Composable
fun ErrorContent(error: String?, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ошибка: ${error ?: "Неизвестная ошибка"}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text("Повторить")
            }
        }
    }
}

// Пустой результат поиска
@Composable
fun EmptyResultContent(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Ничего не найдено",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ничего не найдено",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "По запросу \"$query\" ничего не найдено",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )
        }
    }
}

// Пустое состояние (нет запроса)
@Composable
fun EmptySearchContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Начните поиск",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Введите название трека, исполнителя или альбома",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )
        }
    }
}