package com.qtd.modules.auth.api

import com.qtd.SWAGGER_SECURITY_SCHEMA
import com.qtd.modules.BaseResponse
import com.qtd.modules.BaseResponse.Companion.created
import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.auth.dto.LoginUserRequest
import com.qtd.modules.auth.dto.RefreshTokenRequest
import com.qtd.modules.auth.dto.RegisterUserRequest
import com.qtd.modules.auth.dto.UserCredentialsResponse
import com.qtd.modules.auth.service.IAuthService
import com.qtd.modules.baseRespond
import com.qtd.utils.userId
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.auth() {
    val authService: IAuthService by inject()

    route("/auth", {
        tags = listOf("Auth")
    }) {
        post("/register", {
            description = "Register user"
            request {
                body(RegisterUserRequest::class)
            }
            response {
                HttpStatusCode.Created to {
                    class RegisterResponseType : BaseResponse<UserCredentialsResponse>()
                    body(RegisterResponseType::class)
                }
            }
        }) {
            val registerUser = call.receive<RegisterUserRequest>()
            val newUser = authService.register(registerUser)
            call.baseRespond(created(newUser))
        }

        post("/login", {
            description = "Login user"
            request {
                body(LoginUserRequest::class)
            }
            response {
                HttpStatusCode.Created to {
                    class LoginResponseType : BaseResponse<UserCredentialsResponse>()
                    body(LoginResponseType::class)
                }
            }
        }) {
            val loginUser = call.receive<LoginUserRequest>()
            val user = authService.login(loginUser.user.email, loginUser.user.password)
            call.baseRespond(success(user))
        }

        post("/refresh", {
            description = "Refresh token"
            request {
                body(RefreshTokenRequest::class)
            }
            response {
                HttpStatusCode.Created to {
                    class RefreshResponseType : BaseResponse<UserCredentialsResponse>()
                    body(RefreshResponseType::class)
                }
            }
        }) {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()
            val userCredentials = authService.refresh(refreshTokenRequest.refreshToken)
            call.baseRespond(success(userCredentials))
        }

        authenticate("jwt") {
            delete("/logout", {
                description = "Logout user"
                response {
                    HttpStatusCode.Created to {
                        class LogoutResponseType : BaseResponse<Any>()
                        body(LogoutResponseType::class)
                    }
                }
                securitySchemeName = SWAGGER_SECURITY_SCHEMA
            }) {
                authService.logout(call.userId())
                call.baseRespond(success())
            }
        }

    }
}