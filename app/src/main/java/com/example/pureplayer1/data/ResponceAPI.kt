package com.example.pureplayer1.data

import org.w3c.dom.Entity
import retrofit2.http.GET
import retrofit2.http.Query

interface ResponseAPI {
    @GET("search")
    suspend fun searchMusic(
        @Query("term") term: String?,
        @Query("entity") entity: String? = "song",
        @Query("limit") limit: Int = 10
    ): Response
}