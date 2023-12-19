package com.qtd

import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.SerializationFeature
import com.qtd.config.*
import com.qtd.modules.auth.config.jwtConfig
import com.qtd.modules.auth.services.IPasswordService
import com.qtd.modules.auth.services.ITokenService
import com.qtd.modules.auth.services.PasswordService
import com.qtd.modules.auth.services.TokenService
import com.qtd.modules.database.IDatabaseFactory
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = extractConfig(environment)

    install(Koin) {
        modules(
            module {
                single { config }
                single<ITokenService> { TokenService(get<Config>().jwtConfig) }
                single<JWTVerifier> { get<ITokenService>().getTokenVerifier() }
                single<IPasswordService> { PasswordService }
            },
            serviceKoinModule,
            databaseKoinModule
        )
    }

    val tokenJWTVerifier: JWTVerifier by inject()

    install(DefaultHeaders)
    install(CORS) {
        cors()
    }
    install(CallLogging) {
        level = Level.INFO
    }

    install(Authentication) {
        jwtConfig(config.jwtConfig, tokenJWTVerifier)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val factory: IDatabaseFactory by inject()
    factory.init()

    install(StatusPages) {
        statusPages()
    }

    routing {
        api()
    }
}

fun extractConfig(environment: ApplicationEnvironment) = config {
    applicationConfig {
        port = environment.config.property("ktor.deployment.port").getString().toInt()
    }
    jwtConfig {
        secret = environment.config.property("jwt.secret").getString()
        realm = environment.config.property("jwt.realm").getString()
        issuer = environment.config.property("jwt.issuer").getString()
        audience = environment.config.property("jwt.audience").getString()
    }
    databaseConfig {
        driverClassName = environment.config.property("database.driverClassName").getString()
        jdbcUrl = environment.config.property("database.jdbcUrl").getString()
        maximumPoolSize = environment.config.property("database.maximumPoolSize").getString().toInt()
        isAutoCommit = environment.config.property("database.isAutoCommit").getString().toBoolean()
        transactionIsolation = environment.config.property("database.transactionIsolation").getString()
    }
}
