package com.qtd.config

data class Config(
    val applicationConfig: ApplicationConfig,
    val jwtConfig: JwtConfig,
    val databaseConfig: DatabaseConfig,
)

class ConfigBuilder {
    private lateinit var applicationConfig: ApplicationConfig
    private lateinit var jwtConfig: JwtConfig
    private lateinit var databaseConfig: DatabaseConfig

    fun applicationConfig(block: ApplicationConfigBuilder.() -> Unit) {
        applicationConfig = ApplicationConfigBuilder().apply(block).build()
    }

    fun jwtConfig(block: JwtConfigBuilder.() -> Unit) {
        jwtConfig = JwtConfigBuilder().apply(block).build()
    }

    fun databaseConfig(block: DatabaseBuilder.() -> Unit) {
        databaseConfig = DatabaseBuilder().apply(block).build()
    }

    fun build(): Config = Config(applicationConfig, jwtConfig, databaseConfig)
}


data class ApplicationConfig(
    val port: Int
)

class ApplicationConfigBuilder {
    var port: Int = 0
    fun build(): ApplicationConfig = ApplicationConfig(port)
}


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

data class DatabaseConfig(
    val driverClassName: String,
    val jdbcUrl: String,
    val maximumPoolSize: Int,
    val isAutoCommit: Boolean,
    val transactionIsolation: String
)

class DatabaseBuilder {
    var driverClassName: String = "org.h2.Driver"
    var jdbcUrl: String = "jdbc:h2:file:./gpt.h2"
    var maximumPoolSize: Int = 3
    var isAutoCommit: Boolean = false
    var transactionIsolation: String = "TRANSACTION_REPEATABLE_READ"

    fun build(): DatabaseConfig =
        DatabaseConfig(driverClassName, jdbcUrl, maximumPoolSize, isAutoCommit, transactionIsolation)
}


fun config(block: ConfigBuilder.() -> Unit): Config = ConfigBuilder().apply(block).build()

