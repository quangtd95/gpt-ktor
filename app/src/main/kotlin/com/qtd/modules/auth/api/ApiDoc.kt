package com.qtd.modules.auth.api

import com.qtd.config.ApiDoc
import com.qtd.config.SWAGGER_SECURITY_SCHEMA
import com.qtd.config.TAG_AUTH
import com.qtd.common.BaseResponse
import com.qtd.modules.auth.dto.LoginUserRequest
import com.qtd.modules.auth.dto.RefreshTokenRequest
import com.qtd.modules.auth.dto.RegisterUserRequest
import com.qtd.modules.auth.dto.UserCredentialsResponse
import io.ktor.http.*


val authDoc: ApiDoc = {
    tags = listOf(TAG_AUTH)
}
val registerDoc: ApiDoc = {
    description = "Register user"
    request {
        body(RegisterUserRequest::class)
    }
    response {
        HttpStatusCode.Created to {
            class RegisterResponseType : BaseResponse<UserCredentialsResponse>()
            body(RegisterResponseType::class)
        }
    }
}

val loginDoc: ApiDoc = {
    description = "Login user"
    request {
        body(LoginUserRequest::class)
    }
    response {
        HttpStatusCode.OK to {
            class LoginResponseType : BaseResponse<UserCredentialsResponse>()
            body(LoginResponseType::class)
        }
    }
}

val refreshDoc: ApiDoc = {
    description = "Refresh token"
    request {
        body(RefreshTokenRequest::class)
    }
    response {
        HttpStatusCode.OK to {
            class RefreshResponseType : BaseResponse<UserCredentialsResponse>()
            body(RefreshResponseType::class)
        }
    }
}

val logoutDoc: ApiDoc = {
    description = "Logout user"
    response {
        HttpStatusCode.OK to {
            class LogoutResponseType : BaseResponse<Any>()
            body(LogoutResponseType::class)
        }
    }
    securitySchemeName = SWAGGER_SECURITY_SCHEMA
}