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
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(DefaultHeaders)
    install(CORS) {
        cors()
    }
    install(CallLogging) {
        level = Level.INFO
    }

    val simpleJWT = SimpleJWT(
        secret = this@module.environment.config.property("jwt.secret").getString(),
        realm = this@module.environment.config.property("jwt.realm").getString(),
        issuer = this@module.environment.config.property("jwt.issuer").getString(),
        audience = this@module.environment.config.property("jwt.audience").getString()
    )
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

    routing {
        api(simpleJWT)
    }
}
