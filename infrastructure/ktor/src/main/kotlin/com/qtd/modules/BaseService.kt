package com.qtd.modules

import com.qtd.modules.database.IDatabaseFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseService : KoinComponent {
    private val dbFactory by inject<IDatabaseFactory>()

    suspend fun <T> dbQuery(block: () -> T): T {
        return dbFactory.dbQuery(block)
    }
}