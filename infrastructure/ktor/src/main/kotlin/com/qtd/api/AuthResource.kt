package com.qtd.api

import com.qtd.models.LoginUser
import com.qtd.models.RegisterUser
import com.qtd.models.UpdateUser
import com.qtd.models.UserResponse
import com.qtd.services.IAuthService
import com.qtd.utils.SimpleJWT
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

    get("/users") {
        val users = authService.getAllUsers()
        call.respond(users.map{ UserResponse.fromUser(it)})
    }

    authenticate {
        get("/user") {
            val user = authService.getUserById(call.userId())
            call.respond(UserResponse.fromUser(user))
        }

        put("/user") {
            val updateUser = call.receive<UpdateUser>()
            val user = authService.updateUser(call.userId(), updateUser)
            call.respond(UserResponse.fromUser(user, token = simpleJWT.sign(user.id)))
        }
    }
}