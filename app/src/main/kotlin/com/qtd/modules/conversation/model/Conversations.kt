package com.qtd.modules.conversation.model

import com.qtd.modules.auth.model.Users
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object Conversations : UUIDTable("conversations") {
    val model = varchar("model", 100).nullable()
    val title = varchar("title", 100).nullable()
    val userId = uuid("user_id").references(Users.id)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
}

class Conversation(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Conversation>(Conversations)

    var model by Conversations.model
    var title by Conversations.title
    var userId by Conversations.userId
    var createdAt by Conversations.createdAt
    var deletedAt by Conversations.deletedAt
    val messages by ConversationMessage referrersOn ConversationMessages.conversationId
}