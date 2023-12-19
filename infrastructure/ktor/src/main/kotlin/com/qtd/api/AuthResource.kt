package com.qtd.api

import com.qtd.models.LoginUser
import com.qtd.models.RegisterUser
import com.qtd.models.UserResponse
import com.qtd.services.IAuthService
import com.qtd.utils.SimpleJWT
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.auth(authService: IAuthService, simpleJWT: SimpleJWT) {

    post("/users") {
        val registerUser = call.receive<RegisterUser>()
        val newUser = authService.register(registerUser)
        call.respond(UserResponse.fromUser(newUser, token = simpleJWT.sign(newUser.id)))
    }

    post("/users/login") {
        val loginUser = call.receive<LoginUser>()
        val user = authService.loginAndGetUser(loginUser.user.email, loginUser.user.password)
        call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
    }
}