package com.qtd.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

open class SimpleJWT(
    val realm: String = "realm",
    val secret: String,
    val audience: String = "audience",
    val issuer: String = "issuer",
) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()!!

    fun <T> sign(id: T): String = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("id", id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(algorithm)
}