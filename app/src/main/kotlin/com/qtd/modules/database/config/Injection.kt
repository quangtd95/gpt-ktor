package com.qtd.modules.database.config

import com.qtd.modules.database.DatabaseProvider
import com.qtd.modules.database.IDatabaseProvider
import org.koin.dsl.module

val databaseKoinModule = module {
    single<IDatabaseProvider> { DatabaseProvider() }
}