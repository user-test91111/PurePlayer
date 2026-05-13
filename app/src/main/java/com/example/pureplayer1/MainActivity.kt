package com.example.pureplayer1

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material.icons.outlined.MusicNote
//import androidx.compose.material.icons.filled.Audiotrack
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pureplayer1.data.Track
import com.example.pureplayer1.data.fetchMusic
import com.example.pureplayer1.model.AudioViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController;
    lateinit var viewModel: AudioViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            PlayerState.init(this)
            ThemeManager.loadThemeFromPreferences(this)

            MaterialTheme {
                navController = rememberNavController()
                viewModel = viewModel()

                LaunchedEffect(Unit) {
                    viewModel.loadPopularTracks()
                    viewModel.loadRecommendedTracks()
                }

                val navBackStateEntry by navController.currentBackStackEntryAsState()
                var currentRoute = navBackStateEntry?.destination?.route
                Navigate(navController, currentRoute = currentRoute,viewModel)

                PurePlayerTheme()
            }
        }
        lifecycleScope.launch{
            val tracks = fetchMusic("Gazan")
            for (track in tracks) {
                Log.v("Track", "${track.artist} - ${track.title} - ${track.audioUrl} - ${track.coverUrl}")
            }
        }
    }
}
// Data class для трека (заготовка для API данных)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomBarApp(navController: NavHostController, currentRoute: String?) {
    val density = LocalDensity.current
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(density)
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .height(60.dp + (navigationBarHeight / 2).dp)
            .navigationBarsPadding(), // Автоматически добавит отступы под системные кнопки
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationBarItem(
                selected = currentRoute == Routes.Home.route,
                onClick = { navController.navigate(Routes.Home.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Главная"
                    )
                },
                label = { Text("Главная", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            NavigationBarItem(
                selected = currentRoute == Routes.Search.route,
                onClick = { navController.navigate(Routes.Search.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Поиск"
                    )
                },
                label = { Text("Поиск", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            NavigationBarItem(
                selected = currentRoute == Routes.Favs.route,
                onClick = { navController.navigate(Routes.Favs.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Избранное"
                    )
                },
                label = { Text("Избранное", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            NavigationBarItem(
                selected = currentRoute == Routes.Settings.route,
                onClick = { navController.navigate(Routes.Settings.route) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Настройки"
                    )
                },
                label = { Text("Настройки", fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    currentRoute: String?,
    viewModel: AudioViewModel
) {
    val scrollState = rememberScrollState()

    // Получаем треки из ViewModel
    val popularTracks by viewModel.popularTracks.collectAsStateWithLifecycle()
    val recommendedTracks by viewModel.recommendedTracks.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Заголовок
        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Секция самых популярных
        SectionTitle(
            title = "Самые популярные",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading && popularTracks.isEmpty()) {
            // Показываем индикатор загрузки
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (popularTracks.isNotEmpty()) {
            TrackList(
                tracks = popularTracks,
                onTrackClick = { track ->
                    // Воспроизводим трек с плейлистом популярных треков
                    PlayerState.playTrack(track, popularTracks)
                    navController.navigate(Routes.Player.route)
                },
                onLikeClick = { track ->
                    PlayerState.toggleFavorite(track)
                },
                navController = navController
            )
        } else {
            // Если нет треков, показываем сообщение
            Text(
                text = "Нет популярных треков",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Секция рекомендаций
        SectionTitle(
            title = "Рекомендации",
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading && recommendedTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (recommendedTracks.isNotEmpty()) {
            TrackList(
                tracks = recommendedTracks,
                onTrackClick = { track ->
                    PlayerState.playTrack(track, recommendedTracks)
                    navController.navigate(Routes.Player.route)
                },
                onLikeClick = { track ->
                    PlayerState.toggleFavorite(track)
                },
                navController = navController
            )
        } else {
            Text(
                text = "Нет рекомендаций",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
@Composable
fun HeaderSection() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    val userName = prefs.getString("user_name", "Гость") ?: "Гость"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар пользователя
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (userName != "Гость") userName.take(1).uppercase() else "П",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Добро пожаловать, ",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (userName != "Гость") userName else "Pure Player",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun TrackList(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    onLikeClick: (Track) -> Unit,
    navController: NavHostController
) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        tracks.forEachIndexed { index, track ->
            TrackItem(
                track = track,
                onTrackClick = { onTrackClick(track) },
                onLikeClick = { onLikeClick(track) },
                modifier = Modifier.padding(vertical = 6.dp),
                navController = navController,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItem(
    track: Track,
    onTrackClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,

) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            // Запускаем трек в мини-плеере
            PlayerState.playTrack(track)
//            navController.navigate("player/${track.id}")
            // Передаем событие клика
            onTrackClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Обложка трека
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (!track.coverUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = track.coverUrl,
                        contentDescription = "Обложка трека",
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    // Заглушка если нет URL
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Нет обложки",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                track.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                track.artist?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

//                if (track.playCount > 0) {
//                    Text(
//                        text = "${track.playCount / 1000}K прослушиваний",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.outline,
//                        modifier = Modifier.padding(top = 2.dp)
//                    )
//                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Кнопка лайка
            IconButton(
                onClick = onLikeClick,
                modifier = Modifier.size(48.dp)
            ) {
//                Icon(
//                    imageVector = if (track.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
//                    contentDescription = if (track.isLiked) "Удалить из избранного" else "Добавить в избранное",
//                    tint = if (track.isLiked) MaterialTheme.colorScheme.error
//                    else MaterialTheme.colorScheme.onSurfaceVariant,
//                    modifier = Modifier.size(24.dp)
//                )
            }

//            Text(
//                text = track.duration,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier.padding(start = 4.dp)
//            )
        }
    }
}

// TODO: Функция для загрузки данных из API
// Пример структуры для работы с API:
/*
suspend fun fetchPopularTracks(): List<Track> {
    return try {
        // 1. Создать Retrofit сервис или использовать другой HTTP клиент
        // 2. Определить интерфейс API
        // 3. Использовать suspend функцию для запроса
        // 4. Преобразовать DTO в доменные объекты Track
        // 5. Обработать ошибки

        // Пример с Retrofit:
        // val response = apiService.getPopularTracks()
        // response.tracks.map { it.toTrack() }

        emptyList()
    } catch (e: Exception) {
        // Обработка ошибок
        emptyList()
    }
}

// Пример ViewModel для управления состоянием:
class MusicViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _popularTracks = MutableStateFlow<List<Track>>(emptyList())
    val popularTracks: StateFlow<List<Track>> = _popularTracks.asStateFlow()

    private val _recommendedTracks = MutableStateFlow<List<Track>>(emptyList())
    val recommendedTracks: StateFlow<List<Track>> = _recommendedTracks.asStateFlow()

    fun loadPopularTracks() {
        viewModelScope.launch {
            _popularTracks.value = musicRepository.getPopularTracks()
        }
    }

    fun toggleLike(trackId: String) {
        viewModelScope.launch {
            musicRepository.toggleLike(trackId)
            // Обновить локальное состояние
        }
    }
}
*/


sealed class Routes(val route: String) {

    object Home : Routes("home")
    object Search : Routes("search")
    object Favs : Routes("favs")
    object Player : Routes("player")

    object Settings : Routes("settings")
}

@Composable
fun PurePlayerApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val viewModel: AudioViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Инициализируем плеер
    DisposableEffect(Unit) {
        PlayerState.init(context)
        onDispose {
            PlayerState.release()
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(
                    navController = navController,
                    currentRoute = currentRoute,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                bottomBarApp(navController, currentRoute)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Navigate(navController, currentRoute, viewModel)
        }
    }
}
@Composable
fun Navigate(navController: NavHostController, currentRoute: String?,viewModel: AudioViewModel){
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            MainScreen(navController, viewModel = viewModel, currentRoute = currentRoute)
        }

        composable(Routes.Search.route) {
            SearchScreen(navController = navController, viewModel)
        }

        composable(Routes.Favs.route) {
            FavoritesScreen(navController = navController, currentRoute = currentRoute)
        }

        composable(Routes.Settings.route) {
            SettingsScreen(navController = navController, currentRoute = currentRoute)
        }

        composable(Routes.Player.route) {
            val track = PlayerState.currentTrack
            if (track != null) {
                PlayerScreen(
                    navController = navController,
                    track = track
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}


@Composable
fun PurePlayerTheme() {
    val context = LocalContext.current
    val isDarkTheme by ThemeManager.isDarkTheme.collectAsStateWithLifecycle()

    // Сохраняем тему при изменении
    LaunchedEffect(isDarkTheme) {
        ThemeManager.saveThemeToPreferences(context, isDarkTheme)
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
        typography = Typography(),
        shapes = Shapes()
    ) {
        val navController = rememberNavController()
        val viewModel: AudioViewModel = viewModel()

        LaunchedEffect(Unit) {
            viewModel.loadPopularTracks()
            viewModel.loadRecommendedTracks()
        }

        val navBackStateEntry by navController.currentBackStackEntryAsState()
        var currentRoute = navBackStateEntry?.destination?.route

        PurePlayerApp()
    }
}

// Цветовые схемы
private fun darkColorScheme() = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0x80FFFFFF)
)

private fun lightColorScheme() = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFFE8E8E8),
    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF6B6B6B),
    outline = Color(0x80000000)
)

// Цвета Material Design
private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)
private val Purple40 = Color(0xFF6650a4)
private val PurpleGrey40 = Color(0xFF625b71)
private val Pink40 = Color(0xFF7D5260)