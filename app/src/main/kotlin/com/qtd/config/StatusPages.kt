package com.qtd.config

import com.qtd.exception.AuthenticationException
import com.qtd.common.BaseResponse.Companion.badRequestError
import com.qtd.exception.WrongRequestException
import com.qtd.exception.PermissionException
import com.qtd.common.BaseResponse.Companion.authenticationError
import com.qtd.common.BaseResponse.Companion.permissionError
import com.qtd.common.BaseResponse.Companion.serverError
import com.qtd.common.baseRespond
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.util.*
import org.slf4j.LoggerFactory

@OptIn(InternalAPI::class)
fun StatusPagesConfig.statusPages() {
    val logger = LoggerFactory.getLogger(StatusPagesConfig::class.java)
    exception<Throwable> { call, cause ->
        when (cause) {
            is WrongRequestException -> call.baseRespond(
                badRequestError(
                    cause.message ?: "Bad request error",
                    cause.data
                )
            )

            is BadRequestException -> {
                logger.trace(cause.stackTraceToString())
                call.baseRespond(badRequestError(cause.rootCause?.message ?: cause.message ?: "Bad request error"))
            }

            is PermissionException -> call.baseRespond(permissionError(cause.message ?: "Permission denied"))
            is AuthenticationException -> call.baseRespond(authenticationError(cause.message ?: "Authentication error"))
            else -> {
                logger.trace(cause.stackTraceToString())
                call.baseRespond(serverError(cause.message ?: "Server error"))
            }
        }
    }
}