package com.qtd.modules.auth.dto

import java.time.LocalDateTime

data class Credentials(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiredTime: LocalDateTime,
    val refreshTokenExpiredTime: LocalDateTime,
)