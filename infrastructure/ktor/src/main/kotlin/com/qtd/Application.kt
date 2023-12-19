package com.qtd

import com.qtd.config.api
import com.qtd.config.cors
import com.qtd.config.jwtConfig
import com.qtd.utils.SimpleJWT
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
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
        json()
    }

    routing {
        api(simpleJWT)
    }
}
