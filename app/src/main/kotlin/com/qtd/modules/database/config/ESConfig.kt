package com.qtd.modules.database.config

data class ESConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val https: Boolean,
)

class ESConfigBuilder {
    var host: String = "localhost"
    var port: Int = 9200
    var user: String = ""
    var password: String = ""
    var https: Boolean = false

    fun build(): ESConfig = ESConfig(host, port, user, password, https)
}