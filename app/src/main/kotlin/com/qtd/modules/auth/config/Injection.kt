package com.qtd.modules.auth.config

import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.config.ApplicationConfig
import com.qtd.modules.auth.model.IRefreshTokenDao
import com.qtd.modules.auth.model.IUserDao
import com.qtd.modules.auth.model.RefreshTokens
import com.qtd.modules.auth.model.Users
import com.qtd.modules.auth.service.*
import org.koin.dsl.module

val authKoinModule = module {
    single<ITokenService> { TokenService(get<ApplicationConfig>().jwtConfig) }
    single<JWTVerifier> { get<ITokenService>().getTokenVerifier() }
    single<IPasswordService> { PasswordService }
    single<IRefreshTokenDao> { RefreshTokens }
    single<IUserDao> { Users }
    single<IAuthService> { AuthService() }
}

