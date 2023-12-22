package com.qtd.modules.profile.api

import com.qtd.config.SWAGGER_SECURITY_SCHEMA
import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.baseRespond
import com.qtd.modules.profile.service.IProfileService
import com.qtd.utils.param
import com.qtd.utils.userId
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
            authenticate("jwt", optional = true) {
                get(getProfileDoc) {
                    val username = call.param("username")
                    val currentUserId = call.principal<UserIdPrincipal>()?.name
                    val profile = profileService.getProfile(username, currentUserId)
                    call.baseRespond(success(profile))
                }
            }

            authenticate("jwt") {
                route("/follow", followDoc) {
                    post(postFollowProfileDoc) {
                        val username = call.param("username")
                        val currentUserId = call.userId()
                        val profile = profileService.changeFollowStatus(username, currentUserId, true)
                        call.baseRespond(success(profile))
                    }

                    delete(deleteFollowProfileDoc) {
                        val username = call.param("username")
                        val currentUserId = call.userId()
                        val profile = profileService.changeFollowStatus(username, currentUserId, false)
                        call.baseRespond(success(profile))
                    }
                }

            }
        }

    }

}