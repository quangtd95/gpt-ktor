package com.qtd.modules.profile.api

import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.auth.dto.UpdateUserRequest
import com.qtd.modules.auth.dto.UserResponse
import com.qtd.modules.auth.service.IAuthService
import com.qtd.modules.baseRespond
import com.qtd.utils.Constants.JWT_AUTH
import com.qtd.utils.userId
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.put
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val authService: IAuthService by inject()

    authenticate(JWT_AUTH) {
        route("/users", usersDoc) {
            get(getListUsersDoc) {
                val users = authService.getAllUsers()
                val response = users.map { UserResponse.fromUser(it) }
                call.baseRespond(success(response))
            }

            route("/me") {
                get(getCurrentUserProfileDoc) {
                    val user = authService.getUserById(call.userId())
                    val response = UserResponse.fromUser(user)
                    call.baseRespond(success(response))
                }

                put(updateCurrentUserProfileDoc) {
                    val updateUser = call.receive<UpdateUserRequest>()
                    val user = authService.updateUser(call.userId(), updateUser)
                    val response = UserResponse.fromUser(user)
                    call.baseRespond(success(response))
                }
            }

        }

    }
}