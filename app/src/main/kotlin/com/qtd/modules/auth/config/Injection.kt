package com.qtd.modules.auth.config

import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.config.ApplicationConfig
import com.qtd.modules.auth.models.IRefreshTokenDao
import com.qtd.modules.auth.models.IUserDao
import com.qtd.modules.auth.models.RefreshTokens
import com.qtd.modules.auth.models.Users
import com.qtd.modules.auth.service.*
import com.qtd.modules.profile.services.IProfileService
import com.qtd.modules.profile.services.ProfileService
import org.koin.dsl.module

val authKoinModule = module {
    single<ITokenService> { TokenService(get<ApplicationConfig>().jwtConfig) }
    single<JWTVerifier> { get<ITokenService>().getTokenVerifier() }
    single<IPasswordService> { PasswordService }
    single<IRefreshTokenDao> { RefreshTokens }
    single<IUserDao> { Users }
}

val serviceKoinModule = module {
    single<IAuthService> { AuthService() }
    single<IProfileService> { ProfileService(get()) }
}
