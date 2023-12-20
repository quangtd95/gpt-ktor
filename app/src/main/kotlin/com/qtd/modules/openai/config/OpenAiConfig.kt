package com.qtd.modules.openai.config

data class OpenAiConfig(
    val token: String, val model: String
)

class OpenAiConfigBuilder {
    lateinit var token: String
    lateinit var model: String
    fun build(): OpenAiConfig = OpenAiConfig(token, model)
}

