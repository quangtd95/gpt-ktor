package com.qtd.config

import com.auth0.jwt.interfaces.JWTVerifier
import com.qtd.modules.auth.services.*
import com.qtd.modules.database.DatabaseFactory
import com.qtd.modules.database.IDatabaseFactory
import com.qtd.modules.profile.services.IProfileService
import com.qtd.modules.profile.services.ProfileService
import org.koin.dsl.module

val authKoinModule = module {
    single<ITokenService> { TokenService(get<ApplicationConfig>().jwtConfig) }
    single<JWTVerifier> { get<ITokenService>().getTokenVerifier() }
    single<IPasswordService> { PasswordService }
}

val serviceKoinModule = module {
    single<AuthUseCase> { AuthService() }
    single<IProfileService> { ProfileService(get()) }
}

val databaseKoinModule = module {
    single<IDatabaseFactory> { DatabaseFactory() }
}