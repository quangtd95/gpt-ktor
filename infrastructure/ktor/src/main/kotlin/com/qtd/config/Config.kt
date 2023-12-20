package com.qtd.config

import io.ktor.server.config.*

fun extractConfig(hoconConfig: HoconApplicationConfig) = config {
    val config = hoconConfig.config("app")
    server {
        port = config.property("ktor.deployment.port").getString().toInt()
    }
    jwt {
        secret = config.property("jwt.secret").getString()
        realm = config.property("jwt.realm").getString()
        issuer = config.property("jwt.issuer").getString()
        audience = config.property("jwt.audience").getString()
    }
    database {
        driverClassName = config.property("database.driverClassName").getString()
        jdbcUrl = config.property("database.jdbcUrl").getString()
        maximumPoolSize = config.property("database.maximumPoolSize").getString().toInt()
        isAutoCommit = config.property("database.isAutoCommit").getString().toBoolean()
        transactionIsolation = config.property("database.transactionIsolation").getString()
    }
}