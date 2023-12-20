package com.qtd.modules.auth.api

import com.qtd.modules.auth.service.AuthUseCase
import com.qtd.modules.auth.dto.LoginUser
import com.qtd.modules.auth.dto.RegisterUser
import com.qtd.modules.auth.dto.UpdateUser
import com.qtd.modules.auth.dto.UserResponse
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.auth() {
    val authService: AuthUseCase by inject()

    post("/users") {
        val registerUser = call.receive<RegisterUser>()
        val newUser = authService.register(registerUser)
        call.respond(newUser)
    }

    post("/users/login") {
        val loginUser = call.receive<LoginUser>()
        val user = authService.loginAndGetUser(loginUser.user.email, loginUser.user.password)
        call.respond(user)
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
            val updateUser = call.receive<UpdateUser>()
            val user = authService.updateUser(call.userId(), updateUser)
            call.respond(UserResponse.fromUser(user))
        }
    }
}