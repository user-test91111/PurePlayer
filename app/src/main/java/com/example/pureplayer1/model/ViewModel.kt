package com.example.pureplayer1.model

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pureplayer1.data.Track
import com.example.pureplayer1.data.fetchMusic
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query

//class AudioViewModel(): ViewModel() {
//    var searchTracks = emptyList<Track>()
//    fun searchBySearchBar(query: String){
//        viewModelScope.launch{
//            val tracks = fetchMusic(query)
//            for (track in tracks) {
//                Log.v("Track", "${track.artist} - ${track.title} - ${track.audioUrl} - ${track.coverUrl}")
//            }
//        }
//    }
//}
class AudioViewModel : ViewModel() {
    // Используем mutableStateFlow для автоматического обновления UI
    private val _searchTracks = MutableStateFlow<List<Track>>(emptyList())
    val searchTracks: StateFlow<List<Track>> = _searchTracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun searchBySearchBar(query: String) {
        if (query.isEmpty()) {
            _searchTracks.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val tracks = fetchMusic(query)
                _searchTracks.value = tracks

                for (track in tracks) {
                    Log.v("Track", "${track.artist} - ${track.title} - ${track.audioUrl} - ${track.coverUrl}")
                }
            } catch (e: Exception) {
                _error.value = e.message
                _searchTracks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSearch() {
        _searchTracks.value = emptyList()
    }

}