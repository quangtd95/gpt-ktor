package com.qtd

import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.SerializationFeature
import com.qtd.config.ApplicationConfig
import com.qtd.config.configSwagger
import com.qtd.config.cors
import com.qtd.config.statusPages
import com.qtd.modules.api
import com.qtd.modules.auth.config.jwtConfig
import com.qtd.modules.database.IDatabaseProvider
import com.qtd.modules.database.IESProvider
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.event.Level


fun Application.module() {
    val config: ApplicationConfig by inject()
    val tokenJWTVerifier: JWTVerifier by inject()
    val databaseProvider: IDatabaseProvider by inject()
    val esProvider: IESProvider by inject()

    databaseProvider.init()
    esProvider.init()

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

    install(StatusPages) {
        statusPages()
    }

    install(SwaggerUI) {
        configSwagger()
    }

    routing {
        api()
    }
}


