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

    post("/users") {
        val registerUser = call.receive<RegisterUserRequest>()
        val newUser = authService.register(registerUser)
        call.respond(newUser)
    }

    post("/users/login") {
        val loginUser = call.receive<LoginUserRequest>()
        val user = authService.login(loginUser.user.email, loginUser.user.password)
        call.respond(user)
    }

    post("/users/refresh") {
        val refreshTokenRequest = call.receive<RefreshTokenRequest>()
        val userCredentials = authService.refresh(refreshTokenRequest.refreshToken)
        call.respond(userCredentials)
    }

    get("/users") {
        val users = authService.getAllUsers()
        call.respond(users.map { UserResponse.fromUser(it) })
    }

    authenticate("jwt") {
        get("/user") {
            val user = authService.getUserById(call.userId())
            call.respond(UserResponse.fromUser(user))
        }

        put("/user") {
            val updateUser = call.receive<UpdateUserRequest>()
            val user = authService.updateUser(call.userId(), updateUser)
            call.respond(UserResponse.fromUser(user))
        }
    }
}