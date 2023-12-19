package com.qtd.config

import com.qtd.api.auth
import com.qtd.services.IAuthService
import com.qtd.utils.SimpleJWT
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.api(simpleJWT: SimpleJWT) {
    val authService: IAuthService by inject()
    route("/api") {
        get {
            call.respond("Welcome to GPT World")
        }

        auth(authService, simpleJWT)

        get("/drop") {
            call.respond("OK")
        }
    }
}