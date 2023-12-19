package com.qtd.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userId() = principal<UserIdPrincipal>()?.name ?: throw AuthenticationException()