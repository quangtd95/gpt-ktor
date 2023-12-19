package com.qtd.config

import io.ktor.server.plugins.cors.*
import kotlin.time.Duration.Companion.days

fun CORSConfig.cors() {
    allowMethod(io.ktor.http.HttpMethod.Options)
    allowMethod(io.ktor.http.HttpMethod.Get)
    allowMethod(io.ktor.http.HttpMethod.Post)
    allowMethod(io.ktor.http.HttpMethod.Put)
    allowMethod(io.ktor.http.HttpMethod.Delete)
    allowHeader(io.ktor.http.HttpHeaders.AccessControlAllowHeaders)
    allowHeader(io.ktor.http.HttpHeaders.AccessControlAllowOrigin)
    allowHeader(io.ktor.http.HttpHeaders.Authorization)
    allowCredentials = true
    allowSameOrigin = true
    anyHost()
    maxAgeDuration = 1.days
}