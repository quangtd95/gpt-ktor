package com.qtd.modules.profile.api

import com.qtd.common.BaseResponse.Companion.success
import com.qtd.common.baseRespond
import com.qtd.modules.profile.service.IProfileService
import com.qtd.utils.Constants.JWT_AUTH
import com.qtd.utils.userId
import com.qtd.utils.userIdOrNull
import com.qtd.utils.username
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.profile() {
    val profileService: IProfileService by inject()

    route("/profiles", profilesDoc) {
        route("/{username}") {
            authenticate(JWT_AUTH, optional = true) {
                get(getProfileDoc) {
                    val username = call.username()
                    val currentUserId = call.userIdOrNull()
                    val profile = profileService.getProfile(username, currentUserId)
                    call.baseRespond(success(profile))
                }
            }

            authenticate(JWT_AUTH) {
                route("/follow", followDoc) {
                    post(postFollowProfileDoc) {
                        val username = call.username()
                        val currentUserId = call.userId()
                        val profile = profileService.changeFollowStatus(username, currentUserId, true)
                        call.baseRespond(success(profile))
                    }

                    delete(deleteFollowProfileDoc) {
                        val username = call.username()
                        val currentUserId = call.userId()
                        val profile = profileService.changeFollowStatus(username, currentUserId, false)
                        call.baseRespond(success(profile))
                    }
                }

            }
        }

    }

}