package com.qtd.modules.conversation.dto

data class ConversationResponse(val conversation: Conversation) {
    data class Conversation(
        val id: String, val title: String, val createdAt: String
    )

    companion object {
        fun fromConversation(
            conversation: com.qtd.modules.conversation.model.Conversation
        ): ConversationResponse = ConversationResponse(
            Conversation(
                id = conversation.id.toString(),
                title = conversation.title ?: "",
                createdAt = conversation.createdAt.toString()
            )
        )
    }

}