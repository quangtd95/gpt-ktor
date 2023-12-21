package com.qtd.modules.conversation.api

import com.qtd.modules.conversation.dto.PostChat
import com.qtd.modules.conversation.service.ConversationService
import com.qtd.utils.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
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
                val conversationList = conversationService.getConversations(call.userId())
                call.respond(conversationList)
            }

            delete {
                conversationService.deleteConversations(call.userId())
                call.respond("ok")
            }

            route("/{conversationId}") {

                delete {
                    conversationService.deleteConversation(call.userId(), call.parameters["conversationId"]!!)
                    call.respond("ok")
                }

                route("/messages") {
                    get {
                        val messages =
                            conversationService.getMessages(call.userId(), call.parameters["conversationId"]!!)
                        call.respond(messages)
                    }

                    post {
                        val message = call.receive<PostChat>()
                        val response = conversationService.postMessage(
                            call.userId(), call.parameters["conversationId"]!!, message.content
                        )
                        call.respond(response)
                    }

                    post("/stream") {
                        val message = call.receive<PostChat>()
                        val response = conversationService.postMessageStream(
                            call.userId(), call.parameters["conversationId"]!!, message.content
                        )
                        call.respondSse(response)
                    }
                }
            }
        }
    }
}

/**
 * Method that responds an [ApplicationCall] by reading all the [SseEvent]s from the specified [eventFlow] [Flow]
 * and serializing them in a way that is compatible with the Server-Sent Events specification.
 *
 * You can read more about it here: https://www.html5rocks.com/en/tutorials/eventsource/basics/
 */
suspend fun ApplicationCall.respondSse(eventFlow: Flow<String>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondBytesWriter(contentType = ContentType.Text.EventStream) {
        eventFlow
            .onCompletion {
                flush()
                close()
            }
            .collect { event ->
                for (dataLine in event.lines()) {
                    writeStringUtf8("data: $dataLine\n")
                }
                writeStringUtf8("\n")
                flush()
            }
    }
}
