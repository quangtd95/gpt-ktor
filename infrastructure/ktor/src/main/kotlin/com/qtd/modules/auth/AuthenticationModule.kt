package com.qtd.modules.auth

import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.config.JwtConfig
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun AuthenticationConfig.authenticationModule(
    jwtConfig: JwtConfig,
    tokenVerifier: JWTVerifier
) {
    jwt("jwt") {
        verifier(tokenVerifier)
        realm = jwtConfig.realm

        validate {
            UserIdPrincipal(it.payload.getClaim("id").asString())
        }

        challenge { defaultScheme, realm ->
            call.respond(
                HttpStatusCode.Unauthorized,
                "Token with schema: $defaultScheme, realm= $realm is not valid or has expired"
            )
        }
    }
}
