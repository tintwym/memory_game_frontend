package com.team8.memory_game_frontend.api

import com.team8.memory_game_frontend.data.model.request.AuthRequest
import com.team8.memory_game_frontend.data.model.response.AuthResponse
import com.team8.memory_game_frontend.data.model.request.ScoreRequest
import com.team8.memory_game_frontend.data.model.response.ScoreResponse
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("api/users/{userId}/premium")
    suspend fun purchasePremium(@Path("userId") userId: String): Response<Unit>

    @POST("api/scores/create")
    suspend fun createScore(@Body request: ScoreRequest): ScoreResponse

    @GET("api/scores/get")
    suspend fun getScores(): List<ScoreResponse>
}
