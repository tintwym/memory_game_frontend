package com.team8.memory_game_frontend.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface WebService {
    @GET
    fun fetchPage(
        @Url url: String,
        @Header("User-Agent") userAgent: String = "Mozilla/5.0"
    ): Call<String>
}
