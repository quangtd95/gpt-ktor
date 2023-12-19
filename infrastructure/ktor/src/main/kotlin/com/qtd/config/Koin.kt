package com.qtd.config

import com.qtd.services.AuthService
import com.qtd.services.DatabaseFactory
import com.qtd.services.IAuthService
import com.qtd.services.IDatabaseFactory
import org.koin.dsl.module

val serviceKoinModule = module {
    single<IAuthService> { AuthService(get()) }
}

val databaseKoinModule = module {
    single<IDatabaseFactory> { DatabaseFactory() }
}