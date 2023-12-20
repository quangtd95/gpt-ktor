package com.qtd.modules.database.config

data class DatabaseConfig(
    val driverClassName: String,
    val jdbcUrl: String,
    var username: String,
    var password: String,
    val maximumPoolSize: Int,
    val isAutoCommit: Boolean,
    val transactionIsolation: String
)

class DatabaseConfigBuilder {
    var driverClassName: String = "org.h2.Driver"
    var jdbcUrl: String = "jdbc:h2:file:./gpt.h2"
    var username: String = ""
    var password: String = ""
    var maximumPoolSize: Int = 3
    var isAutoCommit: Boolean = false
    var transactionIsolation: String = "TRANSACTION_REPEATABLE_READ"

    fun build(): DatabaseConfig =
        DatabaseConfig(
            driverClassName,
            jdbcUrl,
            username,
            password,
            maximumPoolSize,
            isAutoCommit,
            transactionIsolation
        )
}