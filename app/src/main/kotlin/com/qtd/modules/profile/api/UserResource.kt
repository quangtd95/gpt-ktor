package com.qtd.modules.profile.api

import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.auth.dto.*
import com.qtd.modules.auth.service.IAuthService
import com.qtd.modules.baseRespond
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val authService: IAuthService by inject()

    authenticate("jwt") {
        get("/users") {
            val users = authService.getAllUsers()
            val response = users.map { UserResponse.fromUser(it) }
            call.baseRespond(success(response))
        }

        get("/users/me") {
            val user = authService.getUserById(call.userId())
            val response = UserResponse.fromUser(user)
            call.baseRespond(success(response))
        }

        put("/users/me") {
            val updateUser = call.receive<UpdateUserRequest>()
            val user = authService.updateUser(call.userId(), updateUser)
            val response = UserResponse.fromUser(user)
            call.baseRespond(success(response))
        }
    }
}