package com.qtd.config

import com.qtd.modules.database.DatabaseFactory
import com.qtd.modules.database.IDatabaseFactory
import com.qtd.modules.auth.services.AuthService
import com.qtd.modules.auth.services.AuthUseCase
import com.qtd.modules.profile.services.IProfileService
import com.qtd.modules.profile.services.ProfileService
import org.koin.dsl.module

val serviceKoinModule = module {
    single<AuthUseCase> { AuthService() }
    single<IProfileService> { ProfileService(get()) }
}

val databaseKoinModule = module {
    single<IDatabaseFactory> { DatabaseFactory() }
}