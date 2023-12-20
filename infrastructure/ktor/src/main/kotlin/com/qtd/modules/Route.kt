package com.qtd.modules

import com.qtd.modules.auth.api.auth
import com.qtd.modules.profile.api.profile
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.api() {

    route("/api") {
        get {
            call.respond("Welcome to GPT World")
        }

        auth()
        profile()

        get("/drop") {
            call.respond("OK")
        }
    }
}