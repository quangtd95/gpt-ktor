package com.qtd.modules.openai.config

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.qtd.config.ApplicationConfig
import com.qtd.modules.openai.service.ChatService
import com.qtd.modules.openai.service.IChatService
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val openaiKoinModule = module {
    single<IChatService> { ChatService }
    single<OpenAI> { getOpenAIInstance(get()) }
}

private fun getOpenAIInstance(config: ApplicationConfig) = OpenAI(
    OpenAIConfig(
        token = config.openAiConfig.token,
        logging = LoggingConfig(
            logLevel = LogLevel.All,
            sanitize = true
        ),
        timeout = Timeout(60.seconds)
    )
)
