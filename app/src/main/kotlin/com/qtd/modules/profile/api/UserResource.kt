package com.qtd.modules.profile.api

import com.qtd.modules.auth.dto.*
import com.qtd.modules.auth.service.IAuthService
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val authService: IAuthService by inject()

    authenticate("jwt") {
        get("/users") {
            val users = authService.getAllUsers()
            call.respond(users.map { UserResponse.fromUser(it) })
        }

        get("/users/me") {
            val user = authService.getUserById(call.userId())
            call.respond(UserResponse.fromUser(user))
        }

        put("/users/me") {
            val updateUser = call.receive<UpdateUserRequest>()
            val user = authService.updateUser(call.userId(), updateUser)
            call.respond(UserResponse.fromUser(user))
        }
    }
}