package com.qtd.modules.conversation.config

import com.qtd.modules.conversation.service.ConversationService
import org.koin.dsl.module

val conversationKoinModule = module {
    single<ConversationService> { ConversationService }
}