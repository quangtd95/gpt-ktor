package com.qtd.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

open class SimpleJWT(
    val realm: String = "realm",
    secret: String,
    audience: String = "audience",
    issuer: String = "issuer",
) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun <T> sign(id: T): String = JWT.create().withClaim("id", id.toString()).sign(algorithm)
}