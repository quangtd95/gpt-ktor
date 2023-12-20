package com.qtd.modules.auth.config

import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun AuthenticationConfig.jwtConfig(
    env: JwtConfig,
    tokenVerifier: JWTVerifier
) {
    jwt("jwt") {
        verifier(tokenVerifier)
        realm = env.realm

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
