package com.qtd.utils

import com.qtd.exception.AccessTokenInvalidException
import com.qtd.exception.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userId() = principal<UserIdPrincipal>()?.name ?: throw AccessTokenInvalidException()

fun ApplicationCall.param(param: String) =
    parameters[param] ?: throw BadRequestException(error = mapOf("param" to listOf("can't be empty")))