package com.qtd

import com.fasterxml.jackson.databind.SerializationFeature
import com.qtd.config.*
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
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val config = extractConfig(environment)
    val simpleJWT = SimpleJWT(config.jwtConfig)

    install(DefaultHeaders)
    install(CORS) {
        cors()
    }
    install(CallLogging) {
        level = Level.INFO
    }

    install(Authentication) {
        jwtConfig(simpleJWT)
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Koin) {
        modules(serviceKoinModule)
        modules(databaseKoinModule)
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
}
