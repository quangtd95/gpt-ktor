package com.qtd

import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.SerializationFeature
import com.qtd.config.*
import com.qtd.modules.auth.ITokenProvider
import com.qtd.modules.auth.TokenProvider
import com.qtd.modules.auth.authenticationModule
import com.qtd.services.IDatabaseFactory
import com.qtd.utils.SimpleJWT
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
    val simpleJWT = SimpleJWT(config.jwtConfig)

    install(Koin) {
        modules(
            module {
                single { config }
                single<ITokenProvider> { TokenProvider(get<Config>().jwtConfig) }
                single<JWTVerifier> { get<ITokenProvider>().getTokenVerifier() }
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
        authenticationModule(config.jwtConfig, tokenJWTVerifier)
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
        api(simpleJWT)
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
