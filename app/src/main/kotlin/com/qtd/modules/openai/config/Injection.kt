package com.qtd.modules.openai.config

import com.qtd.modules.openai.service.ChatService
import com.qtd.modules.openai.service.IChatService
import org.koin.dsl.module

val openaiKoinModule = module {
    single<IChatService> { ChatService }
}