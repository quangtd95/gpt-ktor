package com.qtd

import com.qtd.config.cors
import com.qtd.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(DefaultHeaders)
    install(CORS) {
        cors()
    }

    install(CallLogging) {
        level = Level.INFO
    }
    configureRouting()
}
