package com.qtd.modules.auth.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.modules.auth.config.JwtConfig
import com.qtd.modules.auth.dto.Credentials
import com.qtd.modules.auth.models.User
import java.util.*


interface ITokenService {
    fun createTokens(user: User): Credentials
    fun verifyToken(token: String): String?
    fun getTokenVerifier(): JWTVerifier
}


class TokenService(private val jwtConfig: JwtConfig) : ITokenService {
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

    override fun createTokens(user: User) = Credentials(
        accessToken = createToken(user, Date(System.currentTimeMillis() + validityInMs)),
        refreshToken = createToken(user, Date(System.currentTimeMillis() + refreshValidityInMs))
    )


    override fun verifyToken(token: String): String? {
        return verifier.verify(token).claims["id"]?.asString()
    }

    override fun getTokenVerifier(): JWTVerifier = verifier

}
