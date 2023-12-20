package com.qtd.modules.conversation.model

import com.qtd.modules.auth.model.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object ConversationMessages : UUIDTable("conversation_messages") {
    val conversationId = reference("conversation_id", Conversations)
    val userId = uuid("user_id").references(Users.id)
    val content = text("content")
    val role = varchar("role", 100)
    val createdAt = datetime("created_at")
}

class ConversationMessage(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ConversationMessage>(ConversationMessages)

    var conversationId by ConversationMessages.conversationId
    var userId by ConversationMessages.userId
    var content by ConversationMessages.content
    var role by ConversationMessages.role
    var createdAt by ConversationMessages.createdAt
}