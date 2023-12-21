package com.qtd.config

import io.ktor.server.plugins.cors.*
import kotlin.time.Duration.Companion.days

fun CORSConfig.cors() {
    allowMethod(io.ktor.http.HttpMethod.Options)
    allowMethod(io.ktor.http.HttpMethod.Get)
    allowMethod(io.ktor.http.HttpMethod.Post)
    allowMethod(io.ktor.http.HttpMethod.Put)
    allowMethod(io.ktor.http.HttpMethod.Delete)
    allowMethod(io.ktor.http.HttpMethod.Patch)
    allowMethod(io.ktor.http.HttpMethod.Head)
    allowHeader(io.ktor.http.HttpHeaders.AccessControlAllowHeaders)
    allowHeader(io.ktor.http.HttpHeaders.AccessControlAllowOrigin)
    allowHeader(io.ktor.http.HttpHeaders.Authorization)
    allowHeader(io.ktor.http.HttpHeaders.ContentType)
    allowNonSimpleContentTypes = true
    allowCredentials = true
    allowSameOrigin = true

    anyHost()
    maxAgeDuration = 1.days
}