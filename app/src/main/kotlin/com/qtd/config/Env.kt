package com.qtd.config

import com.qtd.modules.auth.config.JwtConfig
import com.qtd.modules.auth.config.JwtConfigBuilder
import com.qtd.modules.database.config.DatabaseConfigBuilder
import com.qtd.modules.database.config.DatabaseConfig
import com.qtd.modules.database.config.ESConfig
import com.qtd.modules.database.config.ESConfigBuilder
import com.qtd.modules.openai.config.OpenAiConfig
import com.qtd.modules.openai.config.OpenAiConfigBuilder

data class ApplicationConfig(
    val serverConfig: ServerConfig,
    val jwtConfig: JwtConfig,
    val databaseConfig: DatabaseConfig,
    val esConfig: ESConfig,
    val openAiConfig: OpenAiConfig,
)

class ApplicationConfigBuilder {
    private lateinit var serverConfig: ServerConfig
    private lateinit var jwtConfig: JwtConfig
    private lateinit var databaseConfig: DatabaseConfig
    private lateinit var esConfig: ESConfig
    private lateinit var openAiConfig: OpenAiConfig

    fun server(block: ServerConfigBuilder.() -> Unit) {
        serverConfig = ServerConfigBuilder().apply(block).build()
    }

    fun jwt(block: JwtConfigBuilder.() -> Unit) {
        jwtConfig = JwtConfigBuilder().apply(block).build()
    }

    fun es(block: ESConfigBuilder.() -> Unit) {
        esConfig = ESConfigBuilder().apply(block).build()
    }

    fun database(block: DatabaseConfigBuilder.() -> Unit) {
        databaseConfig = DatabaseConfigBuilder().apply(block).build()
    }

    fun openai(block: OpenAiConfigBuilder.() -> Unit) {
        openAiConfig = OpenAiConfigBuilder().apply(block).build()
    }

    fun build(): ApplicationConfig = ApplicationConfig(serverConfig, jwtConfig, databaseConfig, esConfig, openAiConfig)
}

data class ServerConfig(
    val port: Int
)

class ServerConfigBuilder {
    var port: Int = 0
    fun build(): ServerConfig = ServerConfig(port)
}


fun config(block: ApplicationConfigBuilder.() -> Unit): ApplicationConfig =
    ApplicationConfigBuilder().apply(block).build()

