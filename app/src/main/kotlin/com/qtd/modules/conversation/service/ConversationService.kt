package com.qtd.modules.conversation.service

import com.qtd.modules.BaseService
import com.qtd.modules.conversation.dto.ConversationResponse
import com.qtd.modules.conversation.model.Conversation
import com.qtd.modules.conversation.model.ConversationMessage
import com.qtd.modules.conversation.model.Conversations
import org.jetbrains.exposed.sql.and
import java.util.*

object ConversationService : BaseService(), IConversationService {
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

    override suspend fun getConversationList(userId: String): List<ConversationResponse> = dbQuery {
        Conversation.find { Conversations.userId eq UUID.fromString(userId) }.toList()
            .map { ConversationResponse.fromConversation(it) }
    }

    override suspend fun getMessages(userId: String, conversationId: String): List<ConversationMessage> {
        val messages = dbQuery {
            Conversation.find {
                Conversations.userId eq UUID.fromString(userId) and (Conversations.id eq UUID.fromString(conversationId))
            }.firstOrNull()?.messages?.toList()
        }
        return messages ?: listOf()
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

    override suspend fun chat(userId: String, conversationId: String, content: String) {
        TODO("Not yet implemented")
    }
}

interface IConversationService {
    suspend fun createConversation(userId: String): ConversationResponse
    suspend fun getConversationList(userId: String): List<ConversationResponse>
    suspend fun getMessages(userId: String, conversationId: String): List<ConversationMessage>
    suspend fun deleteConversations(userId: String): Boolean
    suspend fun deleteConversation(userId: String, conversationId: String): Boolean

    suspend fun chat(userId: String, conversationId: String, content: String)
}