package com.qtd.modules.conversation.service

import com.qtd.modules.BaseService
import com.qtd.modules.conversation.dto.ConversationMessageResponse
import com.qtd.modules.conversation.dto.ConversationResponse
import com.qtd.modules.conversation.model.Conversation
import com.qtd.modules.conversation.model.ConversationMessage
import com.qtd.modules.conversation.model.Conversations
import com.qtd.modules.openai.service.IChatService
import org.jetbrains.exposed.sql.and
import org.koin.core.component.inject
import java.util.*

object ConversationService : BaseService(), IConversationService {
    private val chatService by inject<IChatService>()

    override suspend fun createConversation(userId: String): ConversationResponse {
        val newConversation = dbQuery {
            Conversation.new {
                model = "gpt-3.5-turbo"
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
            Conversation.find { Conversations.userId eq UUID.fromString(userId) }
                .forEach {
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
        userId: String,
        conversationId: String,
        content: String
    ): ConversationMessageResponse {
        val uuidUserId = UUID.fromString(userId)
        var conversation: Conversation? = null
        var messages: List<ConversationMessage>? = listOf()
        dbQuery {
            conversation = Conversation.find {
                Conversations.userId eq uuidUserId and (Conversations.id eq UUID.fromString(conversationId))
            }.firstOrNull()
            conversation?.let { conv ->
                ConversationMessage.new {
                    this.content = content
                    this.role = "user"
                    this.conversation = conv
                    this.userId = uuidUserId
                }
                messages = conv.messages.toList()
            }
        }
        val response = chatService.chat(messages?.toList() ?: listOf())

        val responseConversationMessage = dbQuery {
            ConversationMessage.new {
                this.content = response
                this.role = "assistant"
                this.conversation = conversation ?: throw Exception("Conversation not found")
                this.userId = uuidUserId
            }
        }
        return ConversationMessageResponse.fromConversationMessage(responseConversationMessage)
    }
}


interface IConversationService {
    suspend fun createConversation(userId: String): ConversationResponse
    suspend fun getConversations(userId: String): List<ConversationResponse>
    suspend fun deleteConversations(userId: String): Boolean
    suspend fun deleteConversation(userId: String, conversationId: String): Boolean

    suspend fun getMessages(userId: String, conversationId: String): List<ConversationMessageResponse>
    suspend fun postMessage(userId: String, conversationId: String, content: String): ConversationMessageResponse
}