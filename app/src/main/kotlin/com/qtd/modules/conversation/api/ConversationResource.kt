package com.qtd.modules.conversation.api

import com.qtd.SWAGGER_SECURITY_SCHEMA
import com.qtd.modules.BaseResponse
import com.qtd.modules.BaseResponse.Companion.created
import com.qtd.modules.BaseResponse.Companion.success
import com.qtd.modules.baseRespond
import com.qtd.modules.conversation.dto.ConversationResponse
import com.qtd.modules.conversation.dto.PostChat
import com.qtd.modules.conversation.service.ConversationService
import com.qtd.utils.userId
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
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

    route("/conversations", {
        tags = listOf("Conversation")
        securitySchemeName = SWAGGER_SECURITY_SCHEMA
    }) {

        authenticate("jwt") {

            post("", {
                description = "Create conversation"
                response {
                    HttpStatusCode.Created to {
                        class ConversationResponseType : BaseResponse<ConversationResponse>()
                        body(ConversationResponseType::class)
                    }
                }
            }) {
                val conversation = conversationService.createConversation(call.userId())
                call.baseRespond(created(conversation))
            }

            get("", {
                description = "Get conversations"
                response {
                    HttpStatusCode.OK to {
                        class ConversationResponseType : BaseResponse<ConversationResponse>()
                        body(ConversationResponseType::class)
                    }
                }
            }) {
                val conversationList = conversationService.getConversations(call.userId())
                call.baseRespond(success(conversationList))
            }

            delete("", {
                description = "Delete conversations"
                response {
                    HttpStatusCode.OK to {
                        class SuccessType : BaseResponse<Any>()
                        body(SuccessType::class)
                    }
                }
            }) {
                conversationService.deleteConversations(call.userId())
                call.baseRespond(success())
            }

            route("/{conversationId}") {

                delete {
                    conversationService.deleteConversation(call.userId(), call.parameters["conversationId"]!!)
                    call.baseRespond(success())
                }

                route("/messages") {
                    get {
                        val messages =
                            conversationService.getMessages(call.userId(), call.parameters["conversationId"]!!)
                        call.baseRespond(success(messages))
                    }

                    post {
                        val message = call.receive<PostChat>()
                        val response = conversationService.postMessage(
                            call.userId(), call.parameters["conversationId"]!!, message.content
                        )
                        call.respond(success(response))
                    }

                    post("/stream") {
                        val message = call.receive<PostChat>()
                        val response = conversationService.postMessageStream(
                            call.userId(), call.parameters["conversationId"]!!, message.content
                        )
                        call.respondSse(response)
                    }

                    delete("/{messageId}") {
                        conversationService.deleteMessage(
                            call.userId(), call.parameters["conversationId"]!!, call.parameters["messageId"]!!
                        )
                        call.baseRespond(success())
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
        eventFlow.onCompletion {
            flush()
            close()
        }.collect { event ->
            for (dataLine in event.lines()) {
                writeStringUtf8("data: $dataLine\n")
            }
            writeStringUtf8("\n")
            flush()
        }
    }
}
