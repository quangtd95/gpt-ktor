package com.qtd.modules.profile.api

import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.baseRespond
import com.qtd.modules.profile.service.IProfileService
import com.qtd.utils.param
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.profile() {
    val profileService: IProfileService by inject()

    authenticate("jwt", optional = true) {
        get("/profiles/{username}") {
            val username = call.param("username")
            val currentUserId = call.principal<UserIdPrincipal>()?.name
            val profile = profileService.getProfile(username, currentUserId)
            call.baseRespond(success(profile))
        }
    }

    authenticate("jwt") {
        post("/profiles/{username}/follow") {
            val username = call.param("username")
            val currentUserId = call.userId()
            val profile = profileService.changeFollowStatus(username, currentUserId, true)
            call.baseRespond(success(profile))
        }

        delete("/profiles/{username}/follow") {
            val username = call.param("username")
            val currentUserId = call.userId()
            val profile = profileService.changeFollowStatus(username, currentUserId, false)
            call.baseRespond(success(profile))
        }
    }
}