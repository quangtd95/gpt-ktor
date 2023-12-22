package com.qtd.modules.openai.service

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.qtd.config.ApplicationConfig
import com.qtd.modules.openai.dto.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


interface IChatService {
    suspend fun chat(message: List<Message>): String
    suspend fun chatStream(message: List<Message>): Flow<String>
}

object ChatService : KoinComponent, IChatService {
    private val config by inject<ApplicationConfig>()
    private val openAI by inject<OpenAI>()

    override suspend fun chat(message: List<Message>): String {
        val result = openAI.chatCompletion(
            ChatCompletionRequest(model = ModelId(config.openAiConfig.model), messages = listOf(
                ChatMessage(role = ChatRole.System, content = system_instructions)
            ) + message.map { ChatMessage(role = fromString(it.role), content = it.content) })
        )
        return if (result.choices.isNotEmpty()) {
            result.choices[0].message.content ?: "Sorry I don't understand"
        } else {
            "Sorry I don't understand"
        }
    }

    override suspend fun chatStream(message: List<Message>): Flow<String> {
        val result = openAI.chatCompletions(
            ChatCompletionRequest(model = ModelId(config.openAiConfig.model), messages = listOf(
                ChatMessage(role = ChatRole.System, content = system_instructions)
            ) + message.map { ChatMessage(role = fromString(it.role), content = it.content) })
        )
        return result.map {
            it.choices[0].delta.content ?: ""
        }
    }
}

val system_instructions = """
    You are a fun assistant, your name is Fun-GPT, you can answer everything concisely. 
    At the end of each answer, add a fun fact or a short joke. 
    Use many icons with a humorous style.
""".trimIndent()

fun fromString(role: String): ChatRole = when (role) {
    "system" -> ChatRole.System
    "user" -> ChatRole.User
    "assistant" -> ChatRole.Assistant
    else -> ChatRole.User
}
