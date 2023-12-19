package com.qtd.services

import com.qtd.models.Followings
import com.qtd.models.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.drop

interface IDatabaseFactory {
    fun init()

    suspend fun <T> dbQuery(block: () -> T): T

    suspend fun drop()
}

class DatabaseFactory : IDatabaseFactory {
    override fun init() {
        Database.connect(hikari())
        transaction {
            create(Users, Followings)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:file:./gpt.h2"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return HikariDataSource(config)

    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

    override suspend fun drop() {
        dbQuery { drop(Users, Followings) }
    }

}