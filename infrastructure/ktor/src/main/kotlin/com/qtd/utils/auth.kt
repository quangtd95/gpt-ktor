package com.qtd.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.qtd.config.JwtConfig
import java.util.*

open class SimpleJWT(val jwtConfig: JwtConfig) {
    private val algorithm = Algorithm.HMAC256(jwtConfig.secret)
    val verifier = JWT
        .require(algorithm)
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()!!

    fun <T> sign(id: T): String = JWT
        .create()
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .withClaim("id", id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(algorithm)
}