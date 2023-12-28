package com.qtd.utils

import com.qtd.exception.AccessTokenInvalidException
import com.qtd.exception.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userIdOrNull() = principal<UserIdPrincipal>()?.name

fun ApplicationCall.userId() = this.userIdOrNull() ?: throw AccessTokenInvalidException()

fun ApplicationCall.param(param: String) =
    parameters[param] ?: throw BadRequestException(error = mapOf("param" to listOf("can't be empty")))

fun ApplicationCall.username() = this.param("username")

fun ApplicationCall.conversationId() = this.param("conversationId")

fun ApplicationCall.messageId() = this.param("messageId")