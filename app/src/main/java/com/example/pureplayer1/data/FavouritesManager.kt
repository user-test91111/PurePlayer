package com.example.pureplayer1

import android.content.Context
import android.content.SharedPreferences
import com.example.pureplayer1.data.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val json = prefs.getString("favorites_list", null)
        if (json != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            _favoriteTracks.value = gson.fromJson(json, type)
        }
    }

    private fun saveFavorites() {
        val json = gson.toJson(_favoriteTracks.value)
        prefs.edit().putString("favorites_list", json).apply()
    }

    fun isFavorite(track: Track): Boolean {
        return _favoriteTracks.value.any { it.title == track.title && it.artist == track.artist }
    }

    fun addToFavorites(track: Track) {
        if (!isFavorite(track)) {
            _favoriteTracks.value = _favoriteTracks.value + track
            saveFavorites()
        }
    }

    fun removeFromFavorites(track: Track) {
        _favoriteTracks.value = _favoriteTracks.value.filter {
            !(it.title == track.title && it.artist == track.artist)
        }
        saveFavorites()
    }

    fun toggleFavorite(track: Track): Boolean {
        return if (isFavorite(track)) {
            removeFromFavorites(track)
            false
        } else {
            addToFavorites(track)
            true
        }
    }

    fun clearFavorites() {
        _favoriteTracks.value = emptyList()
        saveFavorites()
    }
}