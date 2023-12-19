package com.qtd.modules.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.config.JwtConfig
import com.qtd.models.User
import java.util.*


interface ITokenProvider {
    fun createTokens(user: User): CredentialsResponse
    fun verifyToken(token: String): String?
    fun getTokenVerifier(): JWTVerifier
}


class TokenProvider(private val jwtConfig: JwtConfig) : ITokenProvider {
    private val algorithm = Algorithm.HMAC512(jwtConfig.secret)
    private val validityInMs: Long = 3600000L * 24L // 24h
    private val refreshValidityInMs: Long = 3600000L * 24L * 30L // 30 days

    private val verifier = JWT
        .require(algorithm)
        .withAudience(jwtConfig.audience)
        .withIssuer(jwtConfig.issuer)
        .build()!!


    private fun createToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtConfig.issuer)
        .withAudience(jwtConfig.audience)
        .withClaim("id", user.id.toString())
        .withClaim("name", user.username)
        .withExpiresAt(expiration)
        .sign(algorithm)

    override fun createTokens(user: User) = CredentialsResponse(
        accessToken = createToken(user, Date(System.currentTimeMillis() + validityInMs)),
        refreshToken = createToken(user, Date(System.currentTimeMillis() + refreshValidityInMs))
    )


    override fun verifyToken(token: String): String? {
        return verifier.verify(token).claims["id"]?.asString()
    }

    override fun getTokenVerifier(): JWTVerifier = verifier

}
