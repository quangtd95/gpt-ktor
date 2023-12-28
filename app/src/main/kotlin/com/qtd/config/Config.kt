package com.qtd.config

import io.ktor.server.config.*

fun extractConfig(hoconConfig: HoconApplicationConfig) = config {
    server {
        port = hoconConfig.config("ktor.deployment").property("port").getString().toInt()
    }
    jwt {
        with(hoconConfig.config("jwt")) {
            accessTokenSecretKey = property("accessTokenSecretKey").getString()
            refreshTokenSecretKey = property("refreshTokenSecretKey").getString()
            realm = property("realm").getString()
            issuer = property("issuer").getString()
            audience = property("audience").getString()
        }
    }
    database {
        with(hoconConfig.config("database")) {
            driverClassName = property("driverClassName").getString()
            jdbcUrl = property("jdbcUrl").getString()
            maximumPoolSize = property("maximumPoolSize").getString().toInt()
            isAutoCommit = property("isAutoCommit").getString().toBoolean()
            transactionIsolation = property("transactionIsolation").getString()
        }
    }

    es {
        with(hoconConfig.config("es")) {
            host = property("host").getString()
            port = property("port").getString().toInt()
            user = property("user").getString()
            password = property("password").getString()
            https = property("https").getString().toBoolean()
        }
    }

    openai {
        with(hoconConfig.config("openai")) {
            token = property("token").getString()
            model = property("model").getString()
        }
    }
}