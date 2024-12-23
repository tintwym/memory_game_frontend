package com.team8.memory_game_frontend.data.model.request

data class ScoreRequest(
    val userId: String,
    val totalMoves: Int,
    val totalSeconds: Int
)
