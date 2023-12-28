package com.qtd.modules.database

import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import com.jillesvangurp.ktsearch.createIndex
import com.jillesvangurp.ktsearch.exists
import com.qtd.config.ApplicationConfig
import com.qtd.utils.unless
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface IESProvider {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
    suspend fun drop()
}

class ESProvider : IESProvider, KoinComponent {

    private val applicationConfig by inject<ApplicationConfig>()
    private val esConfig = applicationConfig.esConfig
    private val client = SearchClient(
        KtorRestClient(
            host = esConfig.host,
            port = esConfig.port,
            user = esConfig.user,
            password = esConfig.password,
            https = esConfig.https,
        )
    )

    override fun init() {
        runBlocking(Dispatchers.IO) {
            unless(client.exists("ktor")) {
                client.createIndex("ktor")
            }
        }
    }


    override suspend fun <T> dbQuery(block: () -> T): T {
        TODO()
    }

    override suspend fun drop() {
        TODO()
    }
}