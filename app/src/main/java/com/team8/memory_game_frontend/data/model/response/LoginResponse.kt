package com.team8.memory_game_frontend.data.model.response

data class LoginResponse(
    val userId: String,
    val username: String,
    val isPaidUser: Boolean
)
