package com.team8.memory_game_frontend.data.model.response

data class AuthResponse(
    val userId: String,
    val username: String,
    val isPaidUser: Boolean
)
