package com.qtd.config

import com.qtd.exception.AuthenticationException
import com.qtd.modules.BaseResponse.Companion.badRequestError
import com.qtd.exception.BadRequestException
import com.qtd.exception.PermissionException
import com.qtd.modules.BaseResponse.Companion.authenticationError
import com.qtd.modules.BaseResponse.Companion.permissionError
import com.qtd.modules.BaseResponse.Companion.serverError
import com.qtd.modules.baseRespond
import io.ktor.server.plugins.statuspages.*

fun StatusPagesConfig.statusPages() {
    exception<Throwable> { call, cause ->
        when (cause) {
            is BadRequestException -> call.baseRespond(badRequestError(cause.message ?: "Bad request error", cause.error))
            is PermissionException -> call.baseRespond(permissionError(cause.message ?: "Permission denied"))
            is AuthenticationException -> call.baseRespond(authenticationError(cause.message ?: "Authentication error"))
            is Exception -> call.baseRespond(serverError(cause.message ?: "Server error", cause.stackTraceToString()))
            else -> call.baseRespond(serverError(cause.message ?: "Server error", cause.stackTraceToString()))
        }
    }
}