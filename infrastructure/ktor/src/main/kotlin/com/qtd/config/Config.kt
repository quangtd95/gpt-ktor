package com.qtd.config

data class Config(
    val applicationConfig: ApplicationConfig,
    val jwtConfig: JwtConfig
)

class ConfigBuilder {
    private lateinit var applicationConfig: ApplicationConfig
    private lateinit var jwtConfig: JwtConfig

    fun applicationConfig(block: ApplicationConfigBuilder.() -> Unit) {
        applicationConfig = ApplicationConfigBuilder().apply(block).build()
    }

    fun jwtConfig(block: JwtConfigBuilder.() -> Unit) {
        jwtConfig = JwtConfigBuilder().apply(block).build()
    }

    fun build(): Config = Config(applicationConfig, jwtConfig)
}


data class ApplicationConfig(
    val port: Int
)

class ApplicationConfigBuilder {
    var port: Int = 0
    fun build(): ApplicationConfig = ApplicationConfig(port)
}


data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
)

class JwtConfigBuilder {
    lateinit var secret: String
    lateinit var issuer: String
    lateinit var audience: String
    lateinit var realm: String

    fun build(): JwtConfig = JwtConfig(secret, issuer, audience, realm)
}

fun config(block: ConfigBuilder.() -> Unit): Config = ConfigBuilder().apply(block).build()

