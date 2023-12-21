package com.qtd.modules.auth.api

import com.qtd.modules.auth.dto.*
import com.qtd.modules.auth.service.IAuthService
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.auth() {
    val authService: IAuthService by inject()

    route("/auth") {
        post("/register") {
            val registerUser = call.receive<RegisterUserRequest>()
            val newUser = authService.register(registerUser)
            call.respond(newUser)
        }

        post("/login") {
            val loginUser = call.receive<LoginUserRequest>()
            val user = authService.login(loginUser.user.email, loginUser.user.password)
            call.respond(user)
        }

        post("/refresh") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()
            val userCredentials = authService.refresh(refreshTokenRequest.refreshToken)
            call.respond(userCredentials)
        }

        authenticate("jwt") {
            delete("/logout") {
                authService.logout(call.userId())
                call.respond("ok")
            }
        }

    }
}