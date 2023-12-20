package com.qtd.config

import com.qtd.modules.auth.config.JwtConfig
import com.qtd.modules.auth.config.JwtConfigBuilder
import com.qtd.modules.database.config.DatabaseConfigBuilder
import com.qtd.modules.database.config.DatabaseConfig

data class ApplicationConfig(
    val serverConfig: ServerConfig,
    val jwtConfig: JwtConfig,
    val databaseConfig: DatabaseConfig,
)

class ApplicationConfigBuilder {
    private lateinit var serverConfig: ServerConfig
    private lateinit var jwtConfig: JwtConfig
    private lateinit var databaseConfig: DatabaseConfig

    fun server(block: ServerConfigBuilder.() -> Unit) {
        serverConfig = ServerConfigBuilder().apply(block).build()
    }

    fun jwt(block: JwtConfigBuilder.() -> Unit) {
        jwtConfig = JwtConfigBuilder().apply(block).build()
    }

    fun database(block: DatabaseConfigBuilder.() -> Unit) {
        databaseConfig = DatabaseConfigBuilder().apply(block).build()
    }

    fun build(): ApplicationConfig = ApplicationConfig(serverConfig, jwtConfig, databaseConfig)
}

data class ServerConfig(
    val port: Int
)

class ServerConfigBuilder {
    var port: Int = 0
    fun build(): ServerConfig = ServerConfig(port)
}


fun config(block: ApplicationConfigBuilder.() -> Unit): ApplicationConfig = ApplicationConfigBuilder().apply(block).build()

