package com.qtd.modules.auth.config


data class JwtConfig(
    val secret: String, val issuer: String, val audience: String, val realm: String
)

class JwtConfigBuilder {
    lateinit var secret: String
    lateinit var issuer: String
    lateinit var audience: String
    lateinit var realm: String

    fun build(): JwtConfig = JwtConfig(secret, issuer, audience, realm)
}