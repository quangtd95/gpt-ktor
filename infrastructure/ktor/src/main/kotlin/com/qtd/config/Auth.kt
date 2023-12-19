package  com.qtd.config

import com.qtd.utils.SimpleJWT
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun AuthenticationConfig.jwtConfig(simpleJWT: SimpleJWT) {
    jwt {
        realm = simpleJWT.realm
        verifier(simpleJWT.verifier)
        validate {
            JWTPrincipal(it.payload)
        }
        challenge { defaultScheme, realm ->
            call.respond(
                HttpStatusCode.Unauthorized,
                "Token with schema: $defaultScheme, realm= $realm is not valid or has expired"
            )
        }

    }
}
