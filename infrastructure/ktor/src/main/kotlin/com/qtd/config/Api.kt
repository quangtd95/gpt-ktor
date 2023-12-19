package com.qtd.config

import com.qtd.utils.SimpleJWT
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.api(simpleJWT: SimpleJWT) {
    route("/api") {
        get {
            call.respond("Welcome to GPT World")
        }

        get("/drop") {
            call.respond("OK")
        }
    }
}