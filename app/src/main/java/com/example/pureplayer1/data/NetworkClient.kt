package com.example.pureplayer1.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private const val API_URL = "https://itunes.apple.com/"

    val api: ResponseAPI by lazy {
        Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ResponseAPI::class.java)

    }
}

suspend fun fetchMusic(query: String): List<Track>{
    return withContext(Dispatchers.IO){
        try {
            val response = NetworkClient.api.searchMusic(term = query)
            response.result
        }
        catch (e: Exception){
            Log.v("ERROR", "ты лох")
            e.printStackTrace()
            emptyList()
        }
    }
}