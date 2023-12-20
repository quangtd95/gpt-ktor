package com.qtd.modules.conversation.api

import com.qtd.modules.conversation.service.ConversationService
import com.qtd.utils.userId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.conversation() {
    val conversationService: ConversationService by inject()

    route("/conversations") {

        authenticate("jwt") {

            post {
                val conversation = conversationService.createConversation(call.userId())
                call.respond(conversation)
            }

            get {
                val conversationList = conversationService.getConversationList(call.userId())
                call.respond(conversationList)
            }

            get("/{conversationId}/messages") {
                val messages = conversationService.getMessages(call.userId(), call.parameters["conversationId"]!!)
                call.respond(messages)
            }

            delete {
                conversationService.deleteConversations(call.userId())
                call.respond("ok")
            }

            delete("/{conversationId}") {
                conversationService.deleteConversation(call.userId(), call.parameters["conversationId"]!!)
                call.respond("ok")
            }
        }

    }
}
