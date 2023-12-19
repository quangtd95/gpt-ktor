package com.qtd.services

import com.qtd.config.Config
import com.qtd.models.Followings
import com.qtd.models.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


interface IDatabaseFactory {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
    suspend fun drop()
}

@OptIn(DelicateCoroutinesApi::class)
class DatabaseFactory : IDatabaseFactory, KoinComponent {
    private val config by inject<Config>()
    private val dispatcher: CoroutineDispatcher = newFixedThreadPoolContext(5, "database-pool")

    override fun init() {
        Database.connect(hikari())
        transaction {
            create(Users, Followings)
        }
    }

    private fun hikari(): HikariDataSource {
        val dbConfig = config.databaseConfig
        HikariConfig().run {
            driverClassName = dbConfig.driverClassName
            jdbcUrl = dbConfig.jdbcUrl
            maximumPoolSize = dbConfig.maximumPoolSize
            isAutoCommit = dbConfig.isAutoCommit
            transactionIsolation = dbConfig.transactionIsolation
            return HikariDataSource(this)
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }

    override suspend fun drop() {
        dbQuery { drop(Users, Followings) }
    }

}