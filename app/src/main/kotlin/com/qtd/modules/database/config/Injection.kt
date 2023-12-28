package com.qtd.modules.database.config

import com.qtd.modules.database.DatabaseProvider
import com.qtd.modules.database.ESProvider
import com.qtd.modules.database.IDatabaseProvider
import com.qtd.modules.database.IESProvider
import org.koin.dsl.module

val databaseKoinModule = module {
    single<IDatabaseProvider> { DatabaseProvider() }
}

val esKoinModule = module {
    single<IESProvider> { ESProvider() }
}
