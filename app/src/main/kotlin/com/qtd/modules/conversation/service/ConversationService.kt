package com.qtd.modules.conversation.service

import com.qtd.config.ApplicationConfig
import com.qtd.exception.BadRequestException
import com.qtd.modules.BaseService
import com.qtd.modules.conversation.dto.ConversationMessageResponse
import com.qtd.modules.conversation.dto.ConversationResponse
import com.qtd.modules.conversation.model.Conversation
import com.qtd.modules.conversation.model.ConversationMessage
import com.qtd.modules.conversation.model.Conversations
import com.qtd.modules.openai.dto.Message
import com.qtd.modules.openai.service.IChatService
import kotlinx.coroutines.flow.*
import org.jetbrains.exposed.sql.and
import org.koin.core.component.inject
import java.util.*

object ConversationService : BaseService(), IConversationService {
    private val chatService by inject<IChatService>()
    private val config by inject<ApplicationConfig>()

    override suspend fun createConversation(userId: String): ConversationResponse {
        val newConversation = dbQuery {
            Conversation.new {
                model = config.openAiConfig.model
                title = UUID.randomUUID().toString()
                this.userId = UUID.fromString(userId)
            }
        }
        return ConversationResponse.fromConversation(newConversation)
    }

    override suspend fun getConversations(userId: String): List<ConversationResponse> = dbQuery {
        Conversation.find { Conversations.userId eq UUID.fromString(userId) }.toList()
            .map { ConversationResponse.fromConversation(it) }
    }

    override suspend fun getMessages(userId: String, conversationId: String): List<ConversationMessageResponse> {
        val messages = dbQuery {
            Conversation.find {
                Conversations.userId eq UUID.fromString(userId) and (Conversations.id eq UUID.fromString(conversationId))
            }.firstOrNull()?.messages?.toList()
        }
        return messages?.map { ConversationMessageResponse.fromConversationMessage(it) } ?: listOf()
    }

    override suspend fun deleteConversations(userId: String): Boolean {
        dbQuery {
            Conversation.find { Conversations.userId eq UUID.fromString(userId) }.forEach {
                it.delete()
            }

        }

        return true
    }

    override suspend fun deleteConversation(userId: String, conversationId: String): Boolean {
        dbQuery {
            Conversation.find {
                Conversations.userId eq UUID.fromString(userId) and (Conversations.id eq UUID.fromString(conversationId))
            }.firstOrNull()?.delete()
        }
        return true
    }

    override suspend fun postMessage(
        userId: String, conversationId: String, content: String
    ): ConversationMessageResponse {

        val conversation = getConversation(userId, conversationId)

        conversation.addNewUserMessage(content)

        val response = chatService.chat(conversation.getLastMessages().map { Message(it.role, it.content) })

        val assistantMessage = conversation.addNewAssistantMessage(response)

        return ConversationMessageResponse.fromConversationMessage(assistantMessage)
    }

    override suspend fun postMessageStream(
        userId: String, conversationId: String, content: String
    ): Flow<String> {
        val conversation = getConversation(userId, conversationId)

        conversation.addNewUserMessage(content)

        val responseStream = chatService.chatStream(conversation.getLastMessages().map { Message(it.role, it.content) })

        return flow {
            val assistantMessage = StringBuilder()
            responseStream.onEach {
                assistantMessage.append(it)
            }.onCompletion {
                conversation.addNewAssistantMessage(assistantMessage.toString())
            }.collect {
                emit(it)
            }
        }

    }

    private suspend fun getConversation(userId: String, conversationId: String) = dbQuery {
        Conversation.find {
            Conversations.userId eq UUID.fromString(userId) and (Conversations.id eq UUID.fromString(conversationId))
        }.firstOrNull() ?: throw BadRequestException(
            "Conversation not found",
            mapOf("conversationId" to conversationId)
        )
    }

    private suspend fun Conversation.addNewUserMessage(content: String) = dbQuery {
        val conv = this
        ConversationMessage.new {
            this.content = content
            this.role = "user"
            this.conversation = conv
            this.userId = conv.userId
        }

    }

    private suspend fun Conversation.addNewAssistantMessage(content: String) = dbQuery {
        val conv = this
        ConversationMessage.new {
            this.content = content
            this.role = "assistant"
            this.conversation = conv
            this.userId = conv.userId
        }
    }

    private suspend fun Conversation.getLastMessages(): List<ConversationMessage> = dbQuery {
        messages.toList().takeLast(2)
    }
}


interface IConversationService {
    suspend fun createConversation(userId: String): ConversationResponse
    suspend fun getConversations(userId: String): List<ConversationResponse>
    suspend fun deleteConversations(userId: String): Boolean
    suspend fun deleteConversation(userId: String, conversationId: String): Boolean

    suspend fun getMessages(userId: String, conversationId: String): List<ConversationMessageResponse>
    suspend fun postMessage(userId: String, conversationId: String, content: String): ConversationMessageResponse
    suspend fun postMessageStream(userId: String, conversationId: String, content: String): Flow<String>
}