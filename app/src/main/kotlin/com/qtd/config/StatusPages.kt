package com.qtd.config

import com.qtd.utils.AuthenticationException
import com.qtd.utils.UserDoesNotExists
import com.qtd.utils.UserExists
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun StatusPagesConfig.statusPages() {
    exception<Throwable> { call, cause ->
        when (cause) {
            is AuthenticationException -> call.respond(HttpStatusCode.Unauthorized)
            is UserExists -> call.respond(
                HttpStatusCode.UnprocessableEntity,
                mapOf("errors" to mapOf("user" to listOf("exists")))
            )

            is UserDoesNotExists -> call.respond(HttpStatusCode.NotFound)
        }

    }
}