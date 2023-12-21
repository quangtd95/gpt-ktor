package com.qtd.modules.openai.service

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.qtd.config.ApplicationConfig
import com.qtd.modules.conversation.model.ConversationMessage
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds


interface IChatService {
    suspend fun chat(message: List<ConversationMessage>): String
    suspend fun chatStream(message: List<ConversationMessage>): Flow<String>
}

object ChatService : KoinComponent, IChatService {
    private val config by inject<ApplicationConfig>()
    private val openAI = OpenAI(
        OpenAIConfig(
            token = config.openAiConfig.token,
            logging = LoggingConfig(
                logLevel = LogLevel.All,
                sanitize = true
            ),
            timeout = Timeout(60.seconds)
        )
    )

    override suspend fun chat(message: List<ConversationMessage>): String {
        val result = openAI.chatCompletion(
            ChatCompletionRequest(
                model = ModelId(config.openAiConfig.model),
                messages = message.takeLast(10).map {
                    ChatMessage(
                        role = fromString(it.role),
                        content = it.content
                    )
                } + ChatMessage(
                    role = ChatRole.System,
                    content = """
                    You are a fun assistant, your name is Fun-GPT, you can answer everything concisely. 
                    At the end of each answer, add a fun fact or a short joke. 
                    Use many icons with a humorous style.
                    """.trimIndent()
                ))
        )
        return if (result.choices.isNotEmpty()) {
            result.choices[0].message.content ?: "Sorry I don't understand"
        } else {
            "Sorry I don't understand"
        }
    }

    override suspend fun chatStream(message: List<ConversationMessage>): Flow<String> {
        TODO("Not yet implemented")
    }
}

fun fromString(role: String): ChatRole = when (role) {
    "system" -> ChatRole.System
    "user" -> ChatRole.User
    "assistant" -> ChatRole.Assistant
    else -> ChatRole.User
}
