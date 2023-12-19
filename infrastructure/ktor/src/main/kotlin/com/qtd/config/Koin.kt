package com.qtd.config

import com.qtd.services.*
import org.koin.dsl.module

val serviceKoinModule = module {
    single<IAuthService> { AuthService(get()) }
    single<IProfileService> { ProfileService(get()) }
}

val databaseKoinModule = module {
    single<IDatabaseFactory> { DatabaseFactory() }
}