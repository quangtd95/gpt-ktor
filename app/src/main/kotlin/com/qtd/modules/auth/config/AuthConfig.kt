package com.qtd.modules.auth.config

import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.common.BaseResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun AuthenticationConfig.jwtConfig(
    env: JwtConfig, tokenVerifier: JWTVerifier
) {
    jwt("jwt") {
        verifier(tokenVerifier)
        realm = env.realm

        validate {
            UserIdPrincipal(it.payload.getClaim("id").asString())
        }

        challenge { defaultScheme, realm ->
            val error = "Access token is invalid: realm=$realm, defaultScheme=$defaultScheme"
            val r = BaseResponse.authenticationError(error)
            call.respond(HttpStatusCode.fromValue(r.status), r)
        }
    }
}
