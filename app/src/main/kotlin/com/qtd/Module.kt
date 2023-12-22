package com.qtd

import com.auth0.jwt.interfaces.JWTVerifier
import com.fasterxml.jackson.databind.SerializationFeature
import com.qtd.config.ApplicationConfig
import com.qtd.config.cors
import com.qtd.config.statusPages
import com.qtd.modules.api
import com.qtd.modules.auth.config.jwtConfig
import com.qtd.modules.database.IDatabaseProvider
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
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
    databaseProvider.init()

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
        info {
            title = "Fun-GPT"
            version = "latest"
            description = "Fun-GPT API"
            termsOfService = "http://www.example.com/terms"
            contact {
                name = "Fun-GPT Support"
                url = "https://github.com/quangtd95"
                email = "quang.td95@gmail.com"
            }
            license {
                name = "MIT"
                url = ""
            }
        }
        server {
            url = "http://localhost:8989"
            description = "Development server"
        }
        server {
            url = "https://fungpt.com/"
            description = "Production server"
        }
        securityScheme(SWAGGER_SECURITY_SCHEMA) {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
            bearerFormat = "jwt"
        }
        tag("Auth") {
            description = "Operations about authentication"
        }
        tag("User") {
            description = "Operations about user"
        }
        tag("Conversation") {
            description = "Operations about conversation"
        }
        tag("Profile") {
            description = "Operations about profile"
        }
    }

    routing {
        api()
    }
}

const val SWAGGER_SECURITY_SCHEMA = "BearerJWTAuth"
