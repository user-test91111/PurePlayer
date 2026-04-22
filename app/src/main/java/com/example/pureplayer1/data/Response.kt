package com.example.pureplayer1.data


import com.google.gson.annotations.SerializedName

//data class Track(
//    val id: String = "",
//    @SerializedName("trackName")val title: String?,
//    @SerializedName("artistName")val artist: String?,
//    val duration: String = "",
//    @SerializedName("previewUrl") val coverUrl: String?,
//    @SerializedName("artworkUrl") val audioUrl: String?,
//    val isLiked: Boolean = false,
//    val playCount: Int = 0,
//)

data class Track(
//    val id: String = "",
    @SerializedName("trackName")val title: String?,
    @SerializedName("artistName")val artist: String?,
//    val duration: String = "",
    @SerializedName("artworkUrl100") val coverUrl: String?,
    @SerializedName("previewUrl") val audioUrl: String?,
//    val isLiked: Boolean = false,
//    val playCount: Int = 0,
)

data class Response(
    @SerializedName("results") val result: List<Track>
)

